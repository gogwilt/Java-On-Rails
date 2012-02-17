package browser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;

import javax.swing.JLayeredPane;

/**
 * A {@link LayoutManager} that allows every component to fill the entire
 * container (minus insets). This is similar to {@link BorderLayout.CENTER},
 * except that you can add multiple components and they will all overlap. This
 * is useful when you want all layers in a {@link JLayeredPane} to share the
 * same bounds.
 * 
 * The minimum and preferred sizes are the component-wise maximums of all the
 * components.
 * 
 * Invisible components still contribute to minimum and preferred sizes.
 */
public class FillLayout implements LayoutManager2 {

	/**
	 * Returns the maximum component-wise minumum size. That is: the max of all
	 * the minimum heights and the max of all the minimum widths.
	 */
	@Override
	public Dimension minimumLayoutSize( Container target ) {
		synchronized ( target.getTreeLock() ) {
			int width = 0;
			int height = 0;

			for ( Component c : target.getComponents() ) {
				// if( !c.isVisible() ) continue; // un-comment to ignore invisible components
				Dimension d = c.getMinimumSize();
				width = Math.max( width, d.width );
				height = Math.max( height, d.height );
			}

			Insets insets = target.getInsets();
			width += insets.left + insets.right;
			height += insets.top + insets.bottom;

			return new Dimension( width, height );
		}
	}

	/**
	 * Returns the maximum component-wise preferred size. That is: the max of
	 * all the preferred heights and the max of all the preferred widths.
	 */
	@Override
	public Dimension preferredLayoutSize( Container target ) {
		synchronized ( target.getTreeLock() ) {
			int width = 0;
			int height = 0;

			for ( Component c : target.getComponents() ) {
				// if( !c.isVisible() ) continue; // un-comment to ignore invisible components
				Dimension d = c.getPreferredSize();
				width = Math.max( width, d.width );
				height = Math.max( height, d.height );
			}

			Insets insets = target.getInsets();
			width += insets.left + insets.right;
			height += insets.top + insets.bottom;

			return new Dimension( width, height );
		}
	}

	/**
	 * Sets all components to fill the component
	 */
	@Override
	public void layoutContainer( Container target ) {
		synchronized ( target.getTreeLock() ) {
			Insets insets = target.getInsets();
			int top = insets.top;
			int left = insets.left;
			int width = target.getWidth() - insets.left - insets.right;
			int height = target.getHeight() - insets.top - insets.bottom;

			for ( Component c : target.getComponents() ) {
				c.setBounds( left, top, width, height );
			}
		}
	}

	@Override
	public void addLayoutComponent( Component comp, Object constraints ) {
		// no need to do anything
	}

	@Override
	public void addLayoutComponent( String name, Component comp ) {
		// no need to do anything
	}

	@Override
	public void removeLayoutComponent( Component comp ) {
		// no need to do anything
	}

	/** From {@link BorderLayout} */
	@Override
	public Dimension maximumLayoutSize( Container target ) {
		return new Dimension( Integer.MAX_VALUE, Integer.MAX_VALUE );
	}

	/** From {@link BorderLayout} */
	@Override
	public float getLayoutAlignmentX( Container parent ) {
		return 0.5f;
	}

	/** From {@link BorderLayout} */
	@Override
	public float getLayoutAlignmentY( Container parent ) {
		return 0.5f;
	}

	/** From {@link BorderLayout} */
	@Override
	public void invalidateLayout( Container target ) {
		// nothing to discard
	}

}
