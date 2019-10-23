package org.wayne.db;

/**
 * ARIES (from wiki):
 * log must be written to disk before changes to db written to disk.
 * on restart from crash, ARIES retraces actions of DB before crash,
 * brings DB back to exact state before crash. Then undo transactions
 * still active at crash time. 
 * Changes made to DB while undoing transactions are logged to ensure
 * such action is not repeated.
 * 
 * log entries are sequentially ordered with sequence numbers (SN)
 * 
 * there is a dirty page table (DPT). keeps logs of all pages modified
 * but not yet written to disk, and first sequence number that caused
 * page to be dirty. 
 * 
 * there is a transaction table (TT). keeps all transactions that are
 * active and the last log sequence number entry they caused.
 * 
 * tuple is (SN, TID, PID, REDO, UNDO, PSN)
 * SN   sequence number
 * TID  transaction ID
 * PID  page ID
 * REDO
 * UNDO
 * PSN  previous sequence number
 * 
 * Every transaction implicitly begins with Update and is committed
 * with End of Log entry for transaction.
 * 
 * Compensation Log Record (CLR) is used during recovery undoing of
 * aborted transactions, to record that the undo has been done. CLR
 * is form of (SN, TID, PID, REDO, PSN, Next Undo SN)
 * 
 * * Recovery phases:
 * 
 * Analysis: run through logfile, TT, DPT from beginning of last checkpoint
 * and add all transactions with BEG_TXN, and remove the transaction
 * if corresponding END_TXN seen. Go through DPT and add new entry
 * whenever page is modified but not yet in DPT. 
 * 
 * Redo: from DPT, compute min SN of a dirty page. 
 * 
 * Undo:
 * 
 */
public class WAL {

}
