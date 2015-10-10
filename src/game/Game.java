package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import game.storage.GameData;

public class Game {
    private List<Player> players;
    private GameData data;
    private String XMLFilename;

    public Game(GameData data){
    	this.XMLFilename = data.getXMLFilename();
    	this.data = data;
    	this.players = loadPlayers();
    }

    public Player getPlayer(String playerName) {
        return null;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameData getData(){
    	return data;
    }

    public List<Player> loadPlayers(){
    	SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(this.XMLFilename);
		List<Player> players = new ArrayList<Player>();
		try {
			Document document = builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			//Create all the items from the document, put them in the Map items
			Element playersRoot = rootNode.getChild("gamePlayers");
			for(Element e : playersRoot.getChildren()){
				Player currentPlayer = game.storage.FromXML.readPlayer(e, data);
				players.add(currentPlayer);
			}
			return players;
		}catch (IOException io) {
			System.out.println(io.getMessage());
		}catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
		return null;
    }
}

