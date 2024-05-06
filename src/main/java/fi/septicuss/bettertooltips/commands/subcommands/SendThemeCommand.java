package fi.septicuss.bettertooltips.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import fi.septicuss.bettertooltips.Tooltips;
import fi.septicuss.bettertooltips.commands.TooltipsSubCommand;
import fi.septicuss.bettertooltips.object.theme.Theme;
import fi.septicuss.bettertooltips.object.title.Title;
import fi.septicuss.bettertooltips.object.title.TitleBuilder;
import fi.septicuss.bettertooltips.tooltip.Tooltip;
import fi.septicuss.bettertooltips.utils.Colors;
import fi.septicuss.bettertooltips.utils.Messaging;
import fi.septicuss.bettertooltips.utils.placeholder.Placeholders;

public class SendThemeCommand implements TooltipsSubCommand {

	private Tooltips plugin;

	public SendThemeCommand(Tooltips plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, Command command, String label, String[] args) {

		Player target = null;
		Theme theme = null;
		List<String> extra = Lists.newArrayList();

		if (args.length < 2) {
			warn(sender, "Missing target");
			return;
		}

		target = Bukkit.getPlayerExact(args[1]);

		if (target == null) {
			warn(sender, "Unknown target");
			return;
		}

		if (args.length < 3) {
			warn(sender, "Missing theme id");
			return;
		}

		if (!plugin.getThemeManager().doesThemeExist(args[2])) {
			warn(sender, "Theme does not exist");
			return;
		}

		theme = plugin.getThemeManager().getTheme(args[2]);

		if (args.length > 3) {
			StringBuilder builder = new StringBuilder();

			for (int i = 3; i < args.length; i++) {
				builder.append(args[i] + " ");
			}

			String text = Placeholders.replacePlaceholders(target, builder.toString().trim());
			extra.addAll(Arrays.asList(text.split("\\\\n")));
		}

		// Sending

		Tooltip tooltip = plugin.getTooltipManager().getTooltip(target, theme, extra);

		TitleBuilder titleBuilder = new TitleBuilder(plugin.getProtocolManager());
		titleBuilder.setSubtitle(tooltip.getComponents());
		titleBuilder.setFadeIn(0);
		titleBuilder.setStay(5 * 20);
		titleBuilder.setFadeOut(0);

		Title title = titleBuilder.build();
		title.send(target);

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] relativeArgs) {
		switch (relativeArgs.length) {

		case 0:
			return null;
		case 1:
			return null;
		case 2:
			return new ArrayList<>(plugin.getThemeManager().getThemes().keySet());

		}

		return null;
	}
	
	@Override
	public String getPermission() {
		return "tooltips.command.sendtheme";
	}

	private void warn(CommandSender sender, String message) {
		Messaging.send(sender, Colors.WARN + "[!] " + message);
	}

}
