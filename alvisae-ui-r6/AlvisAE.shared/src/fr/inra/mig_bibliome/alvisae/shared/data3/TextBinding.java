/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import java.util.Collection;
import java.util.List;

public interface TextBinding {
	/**
	 * Return the fragments of this annotation.
	 * The returned value is read-only.
	 * Will not return null.
	 * @return
	 */
	Collection<Fragment> getFragments();

	/**
	 * Return a list of the annotation's fragments ordered in the text flow
	 * @return
	 */
  List<Fragment> getSortedFragments();

	/**
	 * Add a fragment to this annotation.
	 * @param fragment
	 * @throws NullPointerException if fragment is null
	 */
	void addFragment(Fragment fragment);
	
	/**
	 * Reduce the text coverage of the specified annotation by the specified logical fragment
	 * @param fragment
	 * @throws NullPointerException if fragment is null
   * @return false if the removal cannot be performed, because it would result in emptying the list of fragments for the specified annotation
	 */
	boolean removeFragment(Fragment fragment);
	
	/**
	 * Add all fragments from the specified collection to this annotation.
	 * @param fragments
	 * @throws NullPointerException if fragments is null
	 */
	void addFragments(Collection<Fragment> fragments);
	
	/**
	 * Reduce the text coverage of the specified annotation by the specified logical fragments
	 * @param fragments
	 * @throws NullPointerException if fragments is null
   * @return false if the removal cannot be performed, because it would result in emptying the list of fragments for the specified annotation
	 */
	boolean removeFragments(Collection<Fragment> fragments);

  /**
	 * Remove all existing fragments fand replace them with the specified collection of fragment.
   * @param fragments
   */
  void setFragments(Collection<Fragment> fragments);

}
