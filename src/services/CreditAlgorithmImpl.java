package services;

public class CreditAlgorithmImpl implements CreditAlgorithm {

	@Override
	public long getCredit(long win, long lose) {
		return win*2-lose;
	}

}
