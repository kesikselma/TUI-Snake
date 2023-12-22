package xd.arkosammy.snake;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameScreen {

    private static GameScreen instance = null;
    public static final int FRAME_DELAY = 50;
    private final Screen terminalScreen;
    private final List<Element> elements = new ArrayList<>();

    private GameScreen() throws IOException {

        Terminal terminal = new DefaultTerminalFactory(System.out, System.in, Charset.defaultCharset()).createTerminal();
        terminal.setForegroundColor(TextColor.ANSI.WHITE);
        terminal.setBackgroundColor(TextColor.ANSI.BLACK);
        terminalScreen = new TerminalScreen(terminal);
        this.terminalScreen.startScreen();
    }

    public static GameScreen getInstance() throws IOException {

        if (instance == null){
            instance = new GameScreen();
        }

        return instance;

    }

    public Screen getTerminalScreen(){
        return this.terminalScreen;
    }

    public Element getElementAtIgnoringSnakeHead(int x, int y){
        for(Element element : this.elements){
            if(element.x() == x && element.y() == y && element.type() != Element.Type.SNAKE_HEAD){
                return element;
            }
        }
        return null;
    }

    public void clearElements(){
        this.elements.clear();
    }

    public void submitElement(Element e){
        if(!elements.contains(e)){
            elements.add(e);
        }
    }

    public void submitAllElements(Collection<Element> points){
        for(Element e : points){
            if(!elements.contains(e)){
                elements.add(e);
            }
        }
    }

    public void refreshDisplay() throws IOException {
        this.terminalScreen.doResizeIfNecessary();
        TextGraphics scoreText = this.terminalScreen.newTextGraphics();
        scoreText.putString(0, 0, String.format("Score: %d", Game.getInstance().getScore()));
        for (int i = this.terminalScreen.getTerminalSize().getRows() - 1; i >= 0; i--) {
            for (int j = 0; j < this.terminalScreen.getTerminalSize().getColumns(); j++) {
                this.terminalScreen.setCharacter(j, i + 1, new TextCharacter(' '));
            }
        }
        for(Element e : this.elements){
            TextCharacter character = new TextCharacter(e.type().getGraphic()).withForegroundColor(e.type().getColor());
            this.terminalScreen.setCharacter(e.x(), e.y() + 1, character);
        }
    }

    public void display() throws InterruptedException, IOException {
        this.terminalScreen.refresh();
        Thread.sleep(FRAME_DELAY);
    }

}