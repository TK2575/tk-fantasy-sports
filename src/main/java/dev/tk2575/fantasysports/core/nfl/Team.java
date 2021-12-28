package dev.tk2575.fantasysports.core.nfl;

import lombok.*;

import java.util.Set;

@Getter
public class Team {
	private int season;
	private String name;
	private Manager manager;
	private Manager coManager;
	private Set<String> priorNames;
}
