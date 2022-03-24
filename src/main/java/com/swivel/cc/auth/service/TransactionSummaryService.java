package com.swivel.cc.auth.service;

import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.exception.AuthServiceException;
import com.swivel.cc.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Transaction Summary Service
 */
@Service
@Slf4j
public class TransactionSummaryService {

    private static final String FAILED_GET_ROLE_USERS = "Getting role users from database was failed";
    private static final String SORT_BY_UPDATED_AT = "updated_at";

    private final UserRepository userRepository;

    @Autowired
    public TransactionSummaryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * This method returns transactions list by bookId and by given options.
     *
     * @param pageable pageable
     * @return list of active transactions
     */
    public Page<User> getRoleUsers(Pageable pageable) {
        Pageable defaultSorted = getDefaultSortedPageable(pageable);
        try {
            //Todo
            throw new UnsupportedOperationException();
        } catch (DataAccessException e) {
            throw new AuthServiceException(FAILED_GET_ROLE_USERS, e);
        }
    }


    /**
     * This method returns transactions list by bookId and by given filters.
     *
     * @param pageable pageable
     * @return list of transactions
     */
    public Page<User> filterRoleUsers(Pageable pageable, int roleId) {
        try {
            //Todo
            Pageable defaultSorted = getDefaultSortedPageable(pageable);
            throw new UnsupportedOperationException();
        } catch (DataAccessException e) {
            throw new AuthServiceException(FAILED_GET_ROLE_USERS, e);
        }
    }


    /**
     * This method returns transactions list by searchTerm and options
     *
     * @param pageable   pageable
     * @param searchTerm searchTerm
     * @return list of transactions
     */
    public Page<User> searchRoleUsers(Pageable pageable, String searchTerm) {
        try {
            Pageable defaultSorted = getDefaultSortedPageable(pageable);
            //Todo
            throw new UnsupportedOperationException();
        } catch (DataAccessException e) {
            throw new AuthServiceException(FAILED_GET_ROLE_USERS, e);
        }
    }

    /**
     * Return the default sort option
     *
     * @param pageable pageable
     * @return pageable
     */
    private Pageable getDefaultSortedPageable(Pageable pageable) {
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.Direction.DESC, SORT_BY_UPDATED_AT);
    }

}
