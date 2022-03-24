package com.swivel.cc.auth.service.roleusersummary;

import com.swivel.cc.auth.configuration.Translator;
import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.service.TransactionSummaryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * This class is used to generate summary data for user name - Search
 */
@AllArgsConstructor
public class Search implements RoleUserSummaryAction {

    private static final String LABEL_TEMPLATE = "SEARCH_LABEL_TEMPLATE";
    private static final String SEARCH_KEY = "<search_value>";
    private TransactionSummaryActionProperty transactionSummaryActionProperty;
    private TransactionSummaryService transactionSummaryService;

    /**
     * This method returns page of transactions for transaction summary option - Search.
     *
     * @return page of transactions
     */
    @Override
    public Page<User> getUsers() {
//        return transactionSummaryService.searchTransactions(
//                transactionSummaryActionProperty.getBookId(),
//                transactionSummaryActionProperty.getPageable(),
//                transactionSummaryActionProperty.getType(),
//                transactionSummaryActionProperty.getStatus().getActive(),
//                transactionSummaryActionProperty.getValue()
//        );
        return null;
    }

    /**
     * This method returns transaction summary display label for search.
     *
     * @return display label
     */
    @Override
    public String getDisplayLabel(Translator translator) {
        String base = transactionSummaryActionProperty.getDefaultDisplayLabel(translator);
        return base +
                translator.toLocale(LABEL_TEMPLATE)
                        .replace(SEARCH_KEY, transactionSummaryActionProperty.getValue());
    }
}
