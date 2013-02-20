package com.turt2live.antishare.pail.pane;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.turt2live.antishare.pail.PailPane;

/**
 * List pane
 * 
 * @author turt2live
 */
public class ListPane extends JPanel {

	private static final long serialVersionUID = -2224535013410395664L;

	public ListPane(){
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(7, 7, 642, 134);
		panel.setBorder(new TitledBorder(null, PailPane.translate("Editable Lists"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel);
		panel.setLayout(new GridLayout(0, 3, 0, 0));

		JButton btnBlockPlace = new JButton(PailPane.translate("Block Place"));
		panel.add(btnBlockPlace);

		JButton btnBlockBreak = new JButton(PailPane.translate("Block Break"));
		panel.add(btnBlockBreak);

		JButton btnDeathItem = new JButton(PailPane.translate("Death Items"));
		panel.add(btnDeathItem);

		JButton btnPickup = new JButton(PailPane.translate("Pickup"));
		panel.add(btnPickup);

		JButton btnDropped = new JButton(PailPane.translate("Dropped Items"));
		panel.add(btnDropped);

		JButton btnRightClick = new JButton(PailPane.translate("Right Click"));
		panel.add(btnRightClick);

		JButton btnUse = new JButton(PailPane.translate("Use"));
		panel.add(btnUse);

		JButton btnCommands = new JButton(PailPane.translate("Commands"));
		panel.add(btnCommands);

		JButton btnAttackableMobs = new JButton(PailPane.translate("Mobs"));
		panel.add(btnAttackableMobs);

		JButton btnRightClickMobs = new JButton(PailPane.translate("Right Click Mobs"));
		panel.add(btnRightClickMobs);

		JButton btnCraftingRecipes = new JButton(PailPane.translate("Crafting Recipes"));
		panel.add(btnCraftingRecipes);

		JButton btnTrackedCreativeBlocks = new JButton(PailPane.translate("Tracked Creative Blocks"));
		panel.add(btnTrackedCreativeBlocks);

		JButton btnTrackedSurvivalBlocks = new JButton(PailPane.translate("Tracked Survival Blocks"));
		panel.add(btnTrackedSurvivalBlocks);

		JButton btnTrackedAdventureBlocks = new JButton(PailPane.translate("Tracked Adventure Blocks"));
		panel.add(btnTrackedAdventureBlocks);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, PailPane.translate("Options"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(12, 146, 637, 357);
		add(panel_1);
	}

}
