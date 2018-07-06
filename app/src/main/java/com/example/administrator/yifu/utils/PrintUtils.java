//package com.example.administrator.yifu.utils;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.CancellationSignal;
//import android.os.ParcelFileDescriptor;
//import android.print.PageRange;
//import android.print.PrintAttributes;
//import android.print.PrintDocumentAdapter;
//import android.print.PrintManager;
//
//import com.example.administrator.yifu.AppController;
//
///**
// * Created by Administrator on 2018/7/5.
// *
// * @author ling
// */
//public class PrintUtils
//{
//
//
//    private void init()
//    {
//        PrintManager printManager = (PrintManager) AppController.getInstance()
//                .getSystemService(Context.PRINT_SERVICE);
//
//
//
//    }
//
//
//    public  class  MyPrintDocumentAdapter extends PrintDocumentAdapter
//    {
//        @Override
//        public void onLayout(publicrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras)
//        {
//
//        }
//
//        @Override
//        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback)
//        {
//            // Iterate over each page of the document,
//            // check if it's in the output range.
//            for (int i = 0; i < totalPages; i++) {
//                // Check to see if this page is in the output range.
//                if (containsPage(pageRanges, i)) {
//                    // If so, add it to writtenPagesArray. writtenPagesArray.size()
//                    // is used to compute the next output page index.
//                    writtenPagesArray.append(writtenPagesArray.size(), i);
//                    PdfDocument.Page page = mPdfDocument.startPage(i);
//
//                    // check for cancellation
//                    if (cancellationSignal.isCancelled()) {
//                        callback.onWriteCancelled();
//                        mPdfDocument.close();
//                        mPdfDocument = null;
//                        return;
//                    }
//
//                    // Draw page content for printing
//                    drawPage(page);
//
//                    // Rendering is complete, so page can be finalized.
//                    mPdfDocument.finishPage(page);
//                }
//            }
//
//            // Write PDF document to file
//            try {
//                mPdfDocument.writeTo(new FileOutputStream(
//                        destination.getFileDescriptor()));
//            } catch (IOException e) {
//                callback.onWriteFailed(e.toString());
//                return;
//            } finally {
//                mPdfDocument.close();
//                mPdfDocument = null;
//            }
//            PageRange[] writtenPages = computeWrittenPages();
//            // Signal the print framework the document is complete
//            callback.onWriteFinished(writtenPages);
//
//        }
//
//
//        private int computePageCount(PrintAttributes printAttributes) {
//            int itemsPerPage = 4; // default item count for portrait mode
//
//            PrintAttributes.MediaSize pageSize = printAttributes.getMediaSize();
//            if (!pageSize.isPortrait()) {
//                // Six items per page in landscape orientation
//                itemsPerPage = 6;
//            }
//
//            // Determine number of print items
//            int printItemCount = 6;
//
//            return (int) Math.ceil(printItemCount / itemsPerPage);
//
//
//        }
//}
