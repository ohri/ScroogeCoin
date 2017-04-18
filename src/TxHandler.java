public class TxHandler {

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */

    private UTXOPool pool;

    public TxHandler(UTXOPool utxoPool) {
        // IMPLEMENT THIS
        pool = new UTXOPool( utxoPool );
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        // IMPLEMENT THIS
        boolean valid = true;

        // make sure all outputs claimed by tx are in the current UTXO pool
        for( int i = 0; i < tx.numOutputs() && valid; i++ )
        {
            if( !pool.contains( new UTXO( tx.getHash(), i ) )  )
            {
                valid = false;
            }
        }

        // TODO - anywhere I'm using outputIndex, I need to reference into pool ... it's hash + index, not index into current hash
        // TODO suspect I'm also using input/output blindly when I should be using pool for some of these

        // validate all sigs on the inpus of tx
        for( int i = 0; i < tx.numInputs() && valid; i++ )
        {
            Transaction.Input theInput = tx.getInput( i );
            if( !Crypto.verifySignature( tx.getOutput( theInput.outputIndex ).address, tx.getRawDataToSign( i ), theInput.signature ) )
            {
                valid = false;
            }
        }

        // TODO make sure no UTXO is claimed multiple times by tx

        // make sure tx output values are non-negative
        for( int i = 0; i < tx.numOutputs() && valid; i++ )
        {
            if( tx.getOutput( i ).value < 0 )
            {
                valid = false;
            }
        }

        // make sure sum of tx input values >= sum of its output values
        if( valid ) {
            double inputSum = 0;
            double outputSum = 0;
            for (int i = 0; i < tx.numOutputs(); i++) {
                outputSum += tx.getOutput(i).value;
            }
            for (int i = 0; i < tx.numInputs(); i++) {
                Transaction.Input theInput = tx.getInput(i);
                inputSum += tx.getOutput(tx.getInput(i).outputIndex).value;
            }
            if (inputSum < outputSum) {
                valid = false;
            }
        }

        return valid;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
    }

}
