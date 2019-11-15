package Strategy;

import Model.Agent;
import Model.AgentAction;
import Model.AgentBomberman;
import Model.Bombe;
import Model.BombermanGame;
import Model.StateBomb;

// Stratégie pour les Bomberman
// Quand un bomberman voit qu'il est aligné avec une bombe au stade 3, il se déplace sur un des cotés pour esquiver si c'est possible
// Si l'agent ne peut pas esquivé ou n'a pas besoin d'esquiver, il fait une action random

public class EsquiveStrategy implements Strategy {

	private static int RANGE_BOMBE_DEFAULT = 2;
	private RandomStrategy randomStrategy = new RandomStrategy();
	
	@Override
	public AgentAction chooseAction(BombermanGame bombermanGame, Agent agent) {		

		for(int i = agent.getX() - RANGE_BOMBE_DEFAULT; i < agent.getX() + RANGE_BOMBE_DEFAULT; ++i) {
			if(bombermanGame.isBomb(i, agent.getY())) { // On regarde s'il y a une bombe
				Bombe bombe = bombermanGame.getBombByCoord(i, agent.getY());
				if(!((AgentBomberman) agent).getListBombe().contains(bombe)) // On verifie que c'est pas la bombe du bomberman
					if(bombe.getStateBomb() == StateBomb.Step3) {
						System.out.println("Agent " + agent.getColor() + " - Esquive en haut ou en bas");
					
						if(agent.isLegalMove(bombermanGame, AgentAction.MOVE_UP)) {
							return AgentAction.MOVE_UP;
						}
						else if(agent.isLegalMove(bombermanGame, AgentAction.MOVE_DOWN)) {
							return  AgentAction.MOVE_DOWN;
						}
					}
			}
		}
		
		for(int i = agent.getY() - RANGE_BOMBE_DEFAULT; i < agent.getY() + RANGE_BOMBE_DEFAULT; ++i) {
			if(bombermanGame.isBomb(agent.getX(),i)) {
				Bombe bombe = bombermanGame.getBombByCoord(agent.getX(),i);
				if(!((AgentBomberman) agent).getListBombe().contains(bombe)) {
					if(bombe.getStateBomb() == StateBomb.Step3) {
						System.out.println("Agent " + agent.getColor() + " - Esquive a droite ou à gauche");
			
						if(agent.isLegalMove(bombermanGame, AgentAction.MOVE_LEFT)) {
							return AgentAction.MOVE_LEFT;
						}
						else if(agent.isLegalMove(bombermanGame, AgentAction.MOVE_RIGHT)) {
							return AgentAction.MOVE_RIGHT;
						}
					}
				}
			}
		}

		return this.randomStrategy.chooseAction(bombermanGame, agent);
	}

}
