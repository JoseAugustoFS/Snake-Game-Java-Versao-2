package engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Entrada implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private EngineCore nucleo;
	
	private boolean[] teclas = new boolean[256];
	private boolean[] ultimas_teclas = new boolean[256];
	private boolean[] botoes = new boolean[5];
	private boolean[] ultimos_botoes = new boolean[5];
	private int mouseX,mouseY,scroll;
	
	public Entrada(EngineCore nucleo)
	{
		this.nucleo=nucleo;
		mouseX=0;
		mouseY=0;
		scroll=0;
		
		nucleo.getWindow().getTela().addKeyListener(this);
		nucleo.getWindow().getTela().addMouseListener(this);
		nucleo.getWindow().getTela().addMouseMotionListener(this);
		nucleo.getWindow().getTela().addMouseWheelListener(this);
	}
	
	public void update()
	{
		scroll=0;
		int tecla_atual;
		for(tecla_atual=0;tecla_atual<256;tecla_atual++)
		{
			ultimas_teclas[tecla_atual]=teclas[tecla_atual];
		}
		int botao_atual;
		for(botao_atual=0;botao_atual<5;botao_atual++)
		{
			ultimos_botoes[botao_atual]=botoes[botao_atual];
		}
	}
	

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		scroll=e.getWheelRotation();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		mouseX= (int)(e.getX()/nucleo.getEscala());
		mouseY= (int)(e.getY()/nucleo.getEscala());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		mouseX= (int)(e.getX()/nucleo.getEscala());
		mouseY= (int)(e.getY()/nucleo.getEscala());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		botoes[e.getButton()]=true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		botoes[e.getButton()]=false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		teclas[e.getKeyCode()]=true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		teclas[e.getKeyCode()]=false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}
	
	// |==============================================================================|
	// Metodos Gets
	
	public int getMouseX()
	{
		return mouseX;
	}
	
	public int getMouseY()
	{
		return mouseY;
	}
	
	public int getScroll()
	{
		return scroll;
	}
	
	// |==============================================================================|
	// Metodos Is
	
	public boolean isTecla(int CodigoTecla)
	{
		return teclas[CodigoTecla];
	}
	
	public boolean isTeclaSolta(int CodigoTecla)
	{
		return !teclas[CodigoTecla] && ultimas_teclas[CodigoTecla];
	}
	
	public boolean isTeclaPressionada(int CodigoTecla)
	{
		return teclas[CodigoTecla] && !ultimas_teclas[CodigoTecla];
	}
	
	public boolean isBotao(int CodigoBotao)
	{
		return botoes[CodigoBotao];
	}
	
	public boolean isBotaoSolto(int CodigoBotao)
	{
		return !botoes[CodigoBotao] && ultimos_botoes[CodigoBotao];
	}
	
	public boolean isBotaoPressionado(int CodigoBotao)
	{
		return botoes[CodigoBotao] && !ultimos_botoes[CodigoBotao];
	}
	
}
