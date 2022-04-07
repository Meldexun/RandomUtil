package meldexun.randomutil;

import java.util.Random;

public class FastRandom extends Random {

	private static final long serialVersionUID = 5655469615636521736L;

	private long seed;

	private static final long multiplier = 0x5DEECE66DL;
	private static final long addend = 0xBL;
	private static final long mask = (1L << 48) - 1;

	public FastRandom() {
		this(seedUniquifier() ^ System.nanoTime());
	}

	private static long seedUniquifier() {
		return seedUniquifier *= 1181783497276652981L;
	}

	private static long seedUniquifier = 8682522807148012L;

	public FastRandom(long seed) {
		super(seed);
	}

	private static long initialScramble(long seed) {
		return (seed ^ multiplier) & mask;
	}

	@Override
	public void setSeed(long seed) {
		this.seed = initialScramble(seed);
		haveNextNextGaussian = false;
	}

	@Override
	protected int next(int bits) {
		return (int) ((seed = (seed * multiplier + addend) & mask) >>> (48 - bits));
	}

	private double nextNextGaussian;
	private boolean haveNextNextGaussian;

	@Override
	public double nextGaussian() {
		if (haveNextNextGaussian) {
			haveNextNextGaussian = false;
			return nextNextGaussian;
		} else {
			double v1, v2, s;
			do {
				v1 = 2 * nextDouble() - 1;
				v2 = 2 * nextDouble() - 1;
				s = v1 * v1 + v2 * v2;
			} while (s >= 1 || s == 0);
			double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
			nextNextGaussian = v2 * multiplier;
			haveNextNextGaussian = true;
			return v1 * multiplier;
		}
	}

}
