package com.swivel.cc.auth.service;

import com.swivel.cc.auth.domain.entity.MerchantBankSearchIndex;
import com.swivel.cc.auth.domain.entity.Role;
import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.enums.RoleType;
import com.swivel.cc.auth.exception.AuthServiceException;
import com.swivel.cc.auth.exception.InvalidRoleException;
import com.swivel.cc.auth.exception.InvalidUserException;
import com.swivel.cc.auth.repository.MerchantBankSearchIndexRepository;
import com.swivel.cc.auth.repository.RoleRepository;
import com.swivel.cc.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * User Role Service
 */
@Service
@Slf4j
public class UserRoleService {

    private static final String FAILED_GET_USER_ROLE = "Getting user or role from database was failed";
    private static final String FAILED_UPDATE_USER_ROLE = "Updating user role to database was failed";
    private static final String FAILED_TO_GET_TOKEN = "Getting token from database was failed";
    private static final String SORT_BY_UPDATED_AT = "updatedAt";
    private static final String SORT_BY_JOINED_ON = "joinedOn";
    private static final String SORT_BY_FULL_NAME = "fullName";
    private static final String ALL = "ALL";
    private final UserRepository userRepository;
    private final JdbcTokenStore tokenStore;
    private final RoleRepository roleRepository;
    private final DefaultTokenServices defaultTokenServices;
    private final MerchantBankSearchIndexRepository merchantBankSearchIndexRepository;


    @Autowired
    public UserRoleService(UserRepository userRepository, JdbcTokenStore tokenStore, RoleRepository roleRepository,
                           DefaultTokenServices defaultTokenServices,
                           MerchantBankSearchIndexRepository merchantBankSearchIndexRepository) {
        this.userRepository = userRepository;
        this.tokenStore = tokenStore;
        this.roleRepository = roleRepository;
        this.defaultTokenServices = defaultTokenServices;
        this.merchantBankSearchIndexRepository = merchantBankSearchIndexRepository;
    }

    /**
     * This method updates the user role.
     *
     * @param userId userId
     * @param roleId roleId
     */
    public void updateUserRole(String userId, int roleId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            Optional<Role> roleOptional = roleRepository.findById(roleId);
            if (userOptional.isEmpty()) {
                throw new InvalidUserException("Invalid user.");
            }
            if (roleOptional.isEmpty()) {
                throw new InvalidRoleException("Invalid role.");
            }
            updateValidUserRole(userOptional.get(), roleOptional.get());
        } catch (DataAccessException e) {
            throw new AuthServiceException(FAILED_GET_USER_ROLE, e);
        }
    }

    /**
     * This method updates the user role and revokes tokens.
     *
     * @param user user
     * @param role role
     */
    private void updateValidUserRole(User user, Role role) {
        try {
            user.setRole(role);
            revokeTokens(user.getId());
            userRepository.save(user);
        } catch (DataAccessException e) {
            throw new AuthServiceException(FAILED_UPDATE_USER_ROLE, e);
        }
    }

    /**
     * This method revokes the token.
     *
     * @param userId userId
     */
    private void revokeTokens(String userId) {
        Collection<OAuth2AccessToken> accessTokens = getTokenByUserId(userId);
        for (OAuth2AccessToken token : new ArrayList<>(accessTokens)) {
            if (token != null) {
                defaultTokenServices.revokeToken(token.getValue());
            }
        }
    }

    /**
     * This method return the token of a user
     *
     * @param userId userId
     * @return OAuth2AccessToken
     */
    private Collection<OAuth2AccessToken> getTokenByUserId(String userId) {
        try {
            return tokenStore.findTokensByUserName(userId);
        } catch (DataAccessException e) {
            throw new AuthServiceException(FAILED_TO_GET_TOKEN, e);
        }
    }

    /**
     * This method filter users by role and firstname.
     *
     * @param roleId     roleId
     * @param searchTerm searchTerm
     * @param pageable   pageable
     * @return filtered users
     */
    public Page<User> getUsers(int roleId, String searchTerm, Pageable pageable) {
        try {
            Pageable defaultSort = getDefaultSortedPageable(pageable);
            if (searchTerm.equals(ALL)) {
                return userRepository.findByRoleId(roleId, defaultSort);
            }
            return userRepository.findByRoleIdAndFullNameContaining(roleId, searchTerm, defaultSort);
        } catch (DataAccessException e) {
            throw new AuthServiceException("Getting users from database failed", e);
        }
    }

    /**
     * This method fetch merchant details.
     *
     * @param searchTerm ALL/business name
     * @param pageable   pageable
     * @return list of merchants.
     */
    public Page<MerchantBankSearchIndex> getMerchantsOrBanks(String searchTerm, Pageable pageable, RoleType userRole) {
        try {
            Sort sort = Sort.by(Sort.Order.desc(SORT_BY_JOINED_ON), Sort.Order.asc(SORT_BY_FULL_NAME));
            Pageable defaultSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
            if (searchTerm.equals(ALL)) {
                return merchantBankSearchIndexRepository.findByUserRole(defaultSort, userRole);
            }
            return merchantBankSearchIndexRepository.getUsersByUserRoleAndSearchTerm(pageable, searchTerm, userRole);
        } catch (DataAccessException e) {
            throw new AuthServiceException("Getting users from database failed", e);
        }
    }

    /**
     * This method validate role by roleId.
     *
     * @param roleId roleId
     */
    private void validateRole(int roleId) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (roleOptional.isEmpty()) {
            throw new InvalidRoleException("Invalid role.");
        }
    }

    /**
     * Return the default sort options
     *
     * @param pageable pageable
     * @return default pageable
     */
    private Pageable getDefaultSortedPageable(Pageable pageable) {
        Sort sort = Sort.by(
                Sort.Order.desc(SORT_BY_UPDATED_AT),
                Sort.Order.asc(SORT_BY_FULL_NAME));
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

}
