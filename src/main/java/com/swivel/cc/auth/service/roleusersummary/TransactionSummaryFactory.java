package com.swivel.cc.auth.service.roleusersummary;

//import com.cc.tokobook.enums.TransactionSummaryOption;
//import com.tokoin.tokobook.exception.TokoBookException;
//import com.tokoin.tokobook.service.TransactionSummaryService;

import com.swivel.cc.auth.enums.TransactionSummaryOption;
import com.swivel.cc.auth.exception.AuthServiceException;
import com.swivel.cc.auth.service.TransactionSummaryService;

/**
 * This is the factory for TransactionSummaryAction.
 */
public class TransactionSummaryFactory {

    /**
     * This method returns relevant TransactionSummaryAction based on the TransactionSummaryOption.
     *
     * @param action                    TransactionSummaryActionProperty
     * @param transactionSummaryService TransactionSummaryService
     * @return relevant TransactionSummaryAction
     */
    public RoleUserSummaryAction getAction(TransactionSummaryActionProperty action,
                                           TransactionSummaryService transactionSummaryService) {
//        TransactionSummaryOption option = action.getOption();
        //todo
        TransactionSummaryOption option = TransactionSummaryOption.SEARCH;
        if (option == null) {
            return new Default(action, transactionSummaryService);
        } else if (option == TransactionSummaryOption.SEARCH) {
            return new Search(action, transactionSummaryService);
        } else if (option == TransactionSummaryOption.FILTER) {
            return new Filter(action, transactionSummaryService);
        } else {
            throw new AuthServiceException("Unsupported TransactionSummaryOption");
        }
    }
}
