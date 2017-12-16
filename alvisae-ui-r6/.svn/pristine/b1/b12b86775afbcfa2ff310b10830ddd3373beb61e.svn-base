/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

/**
 *
 * @author fpapazian
 */
public class ArrayPermuter<T> {
    private int[] state = null;
    private T[] data = null;
    private int permutationOrdNum = 0;

    public ArrayPermuter(T[] data) {
        this.data = data;
        this.state = new int[data.length];
        this.permutationOrdNum = 0;
    }

    public boolean next() {
        //first permutation is identity
        if (permutationOrdNum == 0) {
            permutationOrdNum++;
            return true;
        }
        while (permutationOrdNum < state.length) {
            if (state[permutationOrdNum] < permutationOrdNum) {
                int index = (permutationOrdNum % 2) == 0 ? 0 : state[permutationOrdNum];
                swap(permutationOrdNum, index);
                state[permutationOrdNum]++;
                permutationOrdNum = 1;
                return true;
            } else {
                state[permutationOrdNum] = 0;
                permutationOrdNum++;
            }
        }
        return false;
    }

    private void swap(int i, int j) {
        T tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }
    
}
