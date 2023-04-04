import java.security.*;

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
		RSAKey address = new RSAKey(null, null);
		UTXOPool uniqueUtxos = new UTXOPool();
		double previousTxOutSum = 0;
		// IMPLEMENT THIS
		// (1) FOR LOOP met zoeken in de UTSO pool
		
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
	
			uniqueUtxos.addUTXO(utxo, output);
			previousTxOutSum += output.value;
	
			// Check signature validity
			byte[] message = tx.getRawDataToSign(i);
			if (address.verifySignature(message, in.signature)) {
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
		// (2) kijk of er wel een signature is meegegeven
		// geverifieerd worden met de public, en de data die ondertekend is
		// in de jar file zit een RSA class die je kan gebruiken om te verifiëren met
		// .verify?
		// (3) voorkomt dubbel spending binnen een transactieblok (check distinct!) alle
		// inputs van het tx blok moeten een unieke index hebben
		// (4) alle output values moeten groter zijn dan 0 anders kun je munten stelen
		// van andere met negatieve waardes en bij jezelf dubbele waardes invullen als
		// output
		// lijkt op elevation of privelege of spoofing
		// (5) voor de input moet je de output value pakken van de gelinkte input (dus
		// de output van de vorige transactie)
		// de som van alle input values moet groter zijn dan de som van alle output
		// values
		// de output mag kleiner zijn want scrooch vind het niet erg als er een beetje
		// geld verloren gaat
		// en de output mag niet groter zijn want dan geef je meer geld uit dan je hebt

	/*
	 * Handles each epoch by receiving an unordered array of proposed
	 * transactions, checking each transaction for correctness,
	 * returning a mutually valid array of accepted transactions,
	 * and updating the current UTXO pool as appropriate.
	 */
	public Transaction[] handleTxs(Transaction[] possibleTxs) {
		// IMPLEMENT THIS
		return null;
	}

}
