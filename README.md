# Rankup On Claim

A very simple plugin created because someone needed a way to move users from one group to another when they
claim a region.

This plugin is kinda hacky, in that it listens for command pre-process and checks the command against a regex,
due to the WorldGuard API not providing its own events for this kind of thing.
