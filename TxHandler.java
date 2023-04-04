public class TxHandler {

	/* Creates a public ledger whose current UTXOPool (collection of unspent 
	 * transaction outputs) is utxoPool. This should make a defensive copy of 
	 * utxoPool by using the UTXOPool(UTXOPool uPool) constructor.
	 */
	public TxHandler(UTXOPool utxoPool) {
		// IMPLEMENT THIS
	}

	/* Returns true if 
	 * (1) all outputs claimed by tx are in the current UTXO pool, cannot have the same spend output 2 times, cause then you have double spending
	 * (2) the signatures on each input of tx are valid, 
	 * (3) no UTXO is claimed multiple times by tx, 
	 * (4) all of tx’s output values are non-negative, and
	 * (5) the sum of tx’s input values is greater than or equal to the sum of   
	        its output values;
	   and false otherwise.
	 */

	public boolean isValidTx(Transaction tx) {
		// IMPLEMENT THIS
		// (1) FOR LOOP met zoeken in de UTSO pool
		// (2) kijk of er wel een signature is meegegeven
		// geverifieerd worden met de public, en de data die ondertekend is
		// in de jar file zit een RSA class die je kan gebruiken om te verifiëren met .verify?
		// (3) voorkomt dubbel spending binnen een transactieblok (check distinct!) alle inputs van het tx blok moeten een unieke index hebben
		// (4) alle output values moeten groter zijn dan 0 anders kun je munten stelen van andere met negatieve waardes en bij jezelf dubbele waardes invullen als output
		// lijkt op elevation of privelege of spoofing
		// (5) voor de input moet je de output value pakken van de gelinkte input (dus de output van de vorige transactie) 
		// de som van alle input values moet groter zijn dan de som van alle output values
		// de output mag kleiner zijn want scrooch vind het niet erg als er een beetje geld verloren gaat
		// en de output mag niet groter zijn want dan geef je meer geld uit dan je hebt 
		return false;
	}

	/* Handles each epoch by receiving an unordered array of proposed 
	 * transactions, checking each transaction for correctness, 
	 * returning a mutually valid array of accepted transactions, 
	 * and updating the current UTXO pool as appropriate.
	 */
	public Transaction[] handleTxs(Transaction[] possibleTxs) {
		// IMPLEMENT THIS
		return null;
	}

} 