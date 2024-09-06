package game;

import java.awt.event.KeyEvent;

import engine.EngineCore;
import engine.GameEngine;
import engine.Renderizar;
import engine.audio.ClipSonoro;
import engine.graphics.Imagem;
import engine.graphics.ImagemTile;

public class Main extends GameEngine {

	private Imagem fundo;
	private Imagem maca;
	private Imagem inicio;
	private Imagem vitoria;
	private Imagem derrota;
	private ImagemTile cabeca;
	private ImagemTile corpo;
	private ImagemTile cauda;
	private ClipSonoro plop;
	private ClipSonoro musica;
	
	private Cobra cobra;
	private Colissoes colidir;
	private Maça maça;

	private int direcao=Cobra.DIREITA;
	private boolean atualizar= false, iniciar= false, vencer=false,perder=false;
	private float tempo =0, velocidade = 0.5f;
	
	public Main()
	{
		cobra = new Cobra();
		colidir = new Colissoes();
		maça = new Maça();
		
		fundo = new Imagem("/Fundo.png");
		maca = new Imagem("/Maçã.png");
		maça.DefinirLocal(cobra.getXCorpo(), cobra.getYCorpo(), cobra.getTamanho());
		inicio = new Imagem("/Inicio.png");
		vitoria = new Imagem("/Vitoria.png");
		derrota = new Imagem("/Derrota.png");
		cabeca = new ImagemTile("/Cabeça.png",20,20);
		corpo = new ImagemTile("/Corpo.png",20,20);
		cauda = new ImagemTile("/Cauda.png",20,20);
		
		plop = new ClipSonoro("/Audios/Plop.wav");
		musica = new ClipSonoro("/Audios/Danosongs - Shine Gold Light - Lullaby Mix.wav");
		musica.setVolume(-20f);
	}
	
	@Override
	public void update(EngineCore nucleo, float deltaTime) {
		if(iniciar)
		{			
			if(!musica.isTocando())
				musica.tocar();
			tempo+=deltaTime;
			Teclado(nucleo);
			boolean colissaoGeral,colissaoParede,colissaoCorpo;
			colissaoParede=!(colidir.Parede(cobra.getXCabeca(), cobra.getYCabeca()));
			colissaoCorpo=!(colidir.Corpo(cobra.getXCabeca(), cobra.getYCabeca(), cobra.getXCorpo(), cobra.getYCorpo(), cobra.getTamanho()));
			colissaoGeral=colissaoParede&&colissaoCorpo;
			if((tempo>velocidade)&&(colissaoGeral)&&(cobra.getTamanho()<25))
			{		
				atualizar=true;
				tempo=0;
				
				if((cobra.getXCabeca()==maça.getX())&&(cobra.getYCabeca()==maça.getY()))
				{				
					cobra.setTamanho(cobra.getTamanho()+1);
					maça.DefinirLocal(cobra.getXCorpo(), cobra.getYCorpo(), cobra.getTamanho());
					if(velocidade>0.1)
						velocidade-=0.025;
					plop.tocar();
				}
				
				cobra.DefinirCorpo(direcao);
				
				cobra.DefinirCabeca(direcao);
			}
			perder=!colissaoGeral;
			if(cobra.getTamanho()==25)
				vencer=true;
			if(perder||vencer)
				musica.parar();
		}
		else
		{
			if((nucleo.getEntrada().isBotao(1))&&((nucleo.getEntrada().getMouseX()>=120)&&(nucleo.getEntrada().getMouseX()<=345)&&(nucleo.getEntrada().getMouseY()>=185)&&(nucleo.getEntrada().getMouseY()<=236)))
				iniciar=true;
		}
	}

	@Override
	public void render(EngineCore nucleo, Renderizar renderizar) {
		
		if(iniciar)
		{			
			renderizar.desenharImagem(fundo, 0, 0);	
			renderizar.desenharImagem(maca, (maça.getX()*20), (maça.getY()*20));
			renderizar.desenharTexto("Pontuacao:"+(cobra.getTamanho()-5)+"/20", 20, 5, Renderizar.BLACK);
			int local_atual;
			for(local_atual=1;local_atual<cobra.getTamanho();local_atual++)
			{			
				renderizar.desenharImagemTile(corpo, (cobra.getXCorpo()[local_atual]*20), (cobra.getYCorpo()[local_atual]*20), cobra.getPosCorpo1()[local_atual], cobra.getPosCorpo2()[local_atual]);
			}
			renderizar.desenharImagemTile(cauda, (cobra.getXCorpo()[local_atual]*20), (cobra.getYCorpo()[local_atual]*20), cobra.getPosCauda1(), cobra.getPosCauda2());
			renderizar.desenharImagemTile(cabeca, (cobra.getXCabeca()*20), (cobra.getYCabeca()*20), cobra.getPosCabeca1(), cobra.getPosCabeca2());
		
			if(vencer)
				renderizar.desenharImagem(vitoria, 0, 0);
			if(perder)
				renderizar.desenharImagem(derrota, 0, 0);
		
		}
		else
		{
			renderizar.desenharImagem(inicio, 0, 0);
		}

		
	}
	
	private void Teclado(EngineCore nucleo)
	{
		if(atualizar) 
		{	
			if(nucleo.getEntrada().isTeclaSolta(KeyEvent.VK_W)||nucleo.getEntrada().isTeclaSolta(KeyEvent.VK_UP))
			{
				if(direcao!=Cobra.BAIXO)
				{					
					direcao=Cobra.CIMA;
					atualizar=false;
				}
			}
			if(nucleo.getEntrada().isTeclaSolta(KeyEvent.VK_S)||nucleo.getEntrada().isTeclaSolta(KeyEvent.VK_DOWN))
			{
				if(direcao!=Cobra.CIMA)
				{					
					direcao=Cobra.BAIXO;
					atualizar=false;
				}
			}
			if(nucleo.getEntrada().isTeclaSolta(KeyEvent.VK_D)||nucleo.getEntrada().isTeclaSolta(KeyEvent.VK_RIGHT))
			{
				if(direcao!=Cobra.ESQUERDA)
				{
					direcao=Cobra.DIREITA;
					atualizar=false;					
				}
			}
			if(nucleo.getEntrada().isTeclaSolta(KeyEvent.VK_A)||nucleo.getEntrada().isTeclaSolta(KeyEvent.VK_LEFT))
			{
				if(direcao!=Cobra.DIREITA)
				{
					direcao=Cobra.ESQUERDA;
					atualizar=false;
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		EngineCore nucleo = new EngineCore(new Main());
		nucleo.setTitulo("Snake");
		nucleo.setLargura(480);
		nucleo.setAltura(320);
		nucleo.setEscala(2f);
		nucleo.start();
	}

}
