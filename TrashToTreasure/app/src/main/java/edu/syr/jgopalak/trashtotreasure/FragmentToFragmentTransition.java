/**
 * @FileName FragmentToFragmentTransition.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */

package edu.syr.jgopalak.trashtotreasure;

import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;



public class FragmentToFragmentTransition  extends android.transition.TransitionSet {
    public FragmentToFragmentTransition() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new android.transition.ChangeBounds())
                .addTransition(new ChangeTransform())
                .addTransition(new ChangeImageTransform());
    }
}

