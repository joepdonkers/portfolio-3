import java.util.ArrayList;
import java.util.HashSet;

public class TxHandler {

	private UTXOPool utxoPool;

	/*
	 * Creates a public ledger whose current UTXOPool (collection of unspent
	 * transaction outputs) is utxoPool. This should make a defensive copy of
	 * utxoPool by using the UTXOPool(UTXOPool uPool) constructor.
	 */
	public TxHandler(UTXOPool utxoPool) {
		this.utxoPool = new UTXOPool(utxoPool);
	}

	/*
	 * Returns true if
	 * (1) all outputs claimed by tx are in the current UTXO pool, cannot have the
	 * same spend output 2 times, cause then you have double spending
	 * (2) the signatures on each input of tx are valid,
	 * (3) no UTXO is claimed multiple times by tx,
	 * (4) all of tx’s output values are non-negative, and
	 * (5) the sum of tx’s input values is greater than or equal to the sum of
	 * its output values;
	 * and false otherwise.
	 */
	public boolean isValidTx(Transaction tx) {
		UTXOPool uniqueUtxos = new UTXOPool();
		HashSet<UTXO> seenUtxos = new HashSet<UTXO>();
		double previousTxOutSum = 0;

		for (int i = 0; i < tx.numInputs(); i++) {
			Transaction.Input in = tx.getInput(i);
			UTXO utxo = new UTXO(in.prevTxHash, in.outputIndex);
			Transaction.Output output = utxoPool.getTxOutput(utxo);

			// Check if output is in the current UTXO pool
			if (!utxoPool.contains(utxo)) {
				return false;
			}

			// Check for double spending
			if (uniqueUtxos.contains(utxo)) {
				return false;
			}

			// Check for no UTXO claimed multiple times
			if (seenUtxos.contains(utxo)) {
					return false;
			}
			seenUtxos.add(utxo);

			uniqueUtxos.addUTXO(utxo, output);
			previousTxOutSum += output.value;

			// Check signature validity
			RSAKey address = output.address;
			byte[] message = tx.getRawDataToSign(i);
			if (!address.verifySignature(message, in.signature)) {
				return false;
			}
		}

		double currentTxOutSum = 0;
		for (Transaction.Output out : tx.getOutputs()) {
			if (out.value < 0) {
				return false;
			}
			currentTxOutSum += out.value;
		}

		// Check if output values are non-negative
		if (previousTxOutSum < currentTxOutSum) {
			return false;
		}

		return true;
	}

	/*
	 * Handles each epoch by receiving an unordered array of proposed
	 * transactions, checking each transaction for correctness,
	 * returning a mutually valid array of accepted transactions,
	 * and updating the current UTXO pool as appropriate.
	 */
	public Transaction[] handleTxs(Transaction[] possibleTxs) {
		ArrayList<Transaction> validTxs = new ArrayList<>();

		for (Transaction tx : possibleTxs) {
			if (isValidTx(tx)) {
				validTxs.add(tx);

				// Remove consumed UTXOs
				for (Transaction.Input in : tx.getInputs()) {
					UTXO utxo = new UTXO(in.prevTxHash, in.outputIndex);
					utxoPool.removeUTXO(utxo);
				}

				// Add created UTXOs
				for (int i = 0; i < tx.numOutputs(); i++) {
					Transaction.Output out = tx.getOutput(i);
					UTXO utxo = new UTXO(tx.getHash(), i);
					utxoPool.addUTXO(utxo, out);
				}
			}
		}

		return validTxs.toArray(new Transaction[validTxs.size()]);
	}

}
