//package com.swivel.cc.auth.service.roleusersummary;
//
//import com.tokoin.tokobook.configurations.Translator;
//import com.tokoin.tokobook.domain.entity.Transaction;
//import com.tokoin.tokobook.enums.TransactionSortType;
//import com.tokoin.tokobook.service.TransactionSummaryService;
//import lombok.AllArgsConstructor;
//import org.springframework.data.domain.Page;
//
///**
// * This class is used to generate summary data for
// * the {@link com.tokoin.tokobook.enums.TransactionSummaryOption} - Sort
// */
//@AllArgsConstructor
//public class Sort implements RoleUserSummaryAction {
//
//    private static final String LABEL_TEMPLATE = "SORT_LABEL_TEMPLATE";
//    private static final String SORT_KEY = "<sort_option>";
//    private TransactionSummaryActionProperty transactionSummaryActionProperty;
//    private TransactionSummaryService transactionSummaryService;
//
//    /**
//     * This method returns page of transactions for transaction summary option - Sort.
//     *
//     * @return page of transactions
//     */
//    @Override
//    public Page<Transaction> getUsers() {
//        TransactionSortType sortType = TransactionSortType.getSortType(transactionSummaryActionProperty.getValue());
//        return transactionSummaryService.sortTransactions(
//                transactionSummaryActionProperty.getBookId(),
//                transactionSummaryActionProperty.getPageable(),
//                transactionSummaryActionProperty.getType(),
//                transactionSummaryActionProperty.getStatus().getActive(),
//                sortType
//        );
//    }
//
//    /**
//     * This method returns transaction summary display label for sort.
//     *
//     * @return display label
//     */
//    @Override
//    public String getDisplayLabel(Translator translator) {
//        TransactionSortType sortType = TransactionSortType.getSortType(transactionSummaryActionProperty.getValue());
//        String base = transactionSummaryActionProperty.getDefaultDisplayLabel(translator);
//        return base +
//                translator.toLocale(LABEL_TEMPLATE)
//                        .replace(SORT_KEY, sortType.getDisplayName(translator));
//    }
//}
