package com.swivel.cc.auth.service.roleusersummary;


import com.swivel.cc.auth.configuration.Translator;
import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.service.TransactionSummaryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * This class is used to generate summary data for the default case
 * (No transaction summary option)
 */
@AllArgsConstructor
public class Default implements RoleUserSummaryAction {


    private TransactionSummaryActionProperty transactionSummaryActionProperty;
    private TransactionSummaryService transactionSummaryService;

    /**
     * This method returns page of transactions for the default case.
     *
     * @return page of transactions
     */
    @Override
    public Page<User> getUsers() {
//        return transactionSummaryService.getTransactions(
//                transactionSummaryActionProperty.getBookId(),
//                transactionSummaryActionProperty.getPageable(),
//                transactionSummaryActionProperty.getType(),
//                transactionSummaryActionProperty.getStatus().getActive());
        return null;

    }

    /**
     * This method returns display label for the default case.
     *
     * @return display label
     */
    @Override
    public String getDisplayLabel(Translator translator) {
        return transactionSummaryActionProperty.getDefaultDisplayLabel(translator);
    }


}
