package engine;


import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import engine.graphics.Fonte;
import engine.graphics.Imagem;
import engine.graphics.ImagemTile;
import engine.graphics.Luz;
import engine.graphics.PedidoImagem;
import engine.graphics.PedidoLuz;

public class Renderizar {
	private Fonte fonte=Fonte.ARIAL_11;
	
	
	//private Fonte fonte=Fonte.COMIC_SANS_12;
	//private Fonte fonte=Fonte.PADRAO;
	private ArrayList<PedidoImagem> pedidoI = new ArrayList<PedidoImagem>();
	private ArrayList<PedidoLuz> pedidoL = new ArrayList<PedidoLuz>();

	private int pL,pA;//pixel altura e largura
	private int[] pixel;
	private int[] zBuffer;
	private int[] mapaLuz;
	private int[] blocoLuz;
	private int corAmbiente=CLARO;
	private int profundidadeZ=0;
	private boolean processando=false;
	
	
	public Renderizar(EngineCore nucleo)
	{
		pL=nucleo.getLargura();
		pA=nucleo.getAltura();
		pixel = ((DataBufferInt)nucleo.getWindow().getImage().getRaster().getDataBuffer()).getData();
		zBuffer = new int[pixel.length];
		mapaLuz = new int[pixel.length];
		blocoLuz = new int[pixel.length];
	}
	
	public void limpar()
	{
		int pixel_atual;
		for(pixel_atual=0;pixel_atual<pixel.length;pixel_atual++)
		{
			pixel[pixel_atual]= 0;//== 0x0ff000000 == preto
			zBuffer[pixel_atual]= 0;
			mapaLuz[pixel_atual]=corAmbiente;
			blocoLuz[pixel_atual]=0;
		}
		
	}
	
	public void Processar()
	{
		processando=true;
		
		Collections.sort(pedidoI, new Comparator<PedidoImagem>() {
			@Override
			public int compare(PedidoImagem i0, PedidoImagem i1) {
				if(i0.profundidadeZ>i1.profundidadeZ)
					return 1;
				if(i0.profundidadeZ<i1.profundidadeZ)
					return -1;
				return 0;
			}                                                       
		});
		//Imagem
		int pixel_atual;
		for(pixel_atual=0;pixel_atual<pedidoI.size();pixel_atual++)
		{
			PedidoImagem pi = pedidoI.get(pixel_atual);
			setProfundidadeZ(pi.profundidadeZ);
			desenharImagem(pi.imagem,pi.deslocamentoX,pi.deslocamentoY);
		}
		
		//Luz
		for(pixel_atual=0;pixel_atual<pedidoL.size();pixel_atual++)
		{
			PedidoLuz pl = pedidoL.get(pixel_atual);
			desenharPedidoLuz(pl.luz, pl.locX, pl.locY);
		}
		
		for(pixel_atual=0;pixel_atual<pixel.length;pixel_atual++)
		{
			float vermelho = ((mapaLuz[pixel_atual]>>16)& 0xff)/255f;
			float verde = ((mapaLuz[pixel_atual]>>8)& 0xff)/255f;
			float azul = ((mapaLuz[pixel_atual])& 0xff)/255f;
			
			pixel[pixel_atual]=((int)(((pixel[pixel_atual]>>16)&0xff)*vermelho)<<16|(int)(((pixel[pixel_atual]>>8)&0xff)*verde)<<8|(int)(((pixel[pixel_atual])&0xff)*azul));
		}
		
		pedidoI.clear();
		pedidoL.clear();
		processando=false;
	}
	
	public void setPixel(int x, int y, int valor)
	{
		int alpha =((valor>>24)&0xff);
		
		if(((x<0)||(x>=pL)||(y<0)||(y>=pA))||(alpha==0))
		{			
			return;
		}
		
		int indicador = x+y*pL;
		
		if(zBuffer[indicador]>profundidadeZ)
			return;
		
		zBuffer[indicador]=profundidadeZ;
		
		if(alpha==255)
		{			
			pixel[indicador] = valor;
		}
		else
		{
			int corPixel = pixel[indicador];
			
			int novoVermelho=((corPixel>>16)&0xff)-(int)((((corPixel>>16)&0xff)-((valor>>16)&0xff))*(alpha/255f));
			int novoVerde=((corPixel>>8)&0xff)-(int)((((corPixel>>8)&0xff)-((valor>>8)&0xff))*(alpha/255f));
			int novoAzul=((corPixel)&0xff)-(int)((((corPixel)&0xff)-((valor)&0xff))*(alpha/255f));
			
			pixel[indicador] = (255<<24|novoVermelho<<16|novoVerde<<8|novoAzul);
		}
	}
	
	public void setMapaLuz(int x, int y, int valor)
	{
		if((x<0)||(x>=pL)||(y<0)||(y>=pA))
		{			
			return;
		}
		int corbase=mapaLuz[x+y*pL];
		int VermelhoMaximo = Math.max(((corbase>>16)&0xff), ((valor>>16)&0xff));
		int VerdeMaximo= Math.max(((corbase>>8)&0xff), ((valor>>8)&0xff));
		int AzulMaximo= Math.max(((corbase)&0xff), ((valor)&0xff));
		mapaLuz[x+y*pL] = (VermelhoMaximo<<16|VerdeMaximo<<8|AzulMaximo);
	}
	
	public void setBlocoLuz(int x, int y, int valor, int valor2)
	{
		if((x<0)||(x>=pL)||(y<0)||(y>=pA)||((valor2>>24)&0xff)==0)
		{			
			return;
		}
		if(zBuffer[x+y*pL]>profundidadeZ)
			return;
		
		blocoLuz[x+y*pL] = valor;
	}
	
	public void desenharTexto(String texto, int deslocamentoX, int deslocamentoY, int cor)
	{
		int letra_atual,unicode,deslocamento=0,y,x;
		for(letra_atual=0;letra_atual<texto.length();letra_atual++)
		{
			unicode=texto.codePointAt(letra_atual);
			for(y=0;y<fonte.getImagemFonte().getAltura();y++)
			{
				for(x=0;x<fonte.getLarguras()[unicode];x++)
				{
					if(fonte.getImagemFonte().getPixel()[(x+fonte.getDeslocamentos()[unicode])+y*fonte.getImagemFonte().getLargura()]==0xffffffff)
					{
						setPixel(x+deslocamentoX+deslocamento,y+deslocamentoY,cor);
					}
				}
			}
			deslocamento+=fonte.getLarguras()[unicode];
		}
	}
	
	public void desenharImagem(Imagem imagem, int deslocamentoX, int deslocamentoY)
	{
		if((imagem.isAlpha())&&(!processando))
		{
			pedidoI.add(new PedidoImagem(imagem,profundidadeZ,deslocamentoX,deslocamentoY));
			return;
		}
		
		//Evita tentar renderizar fora da tela
		if(deslocamentoX<-imagem.getLargura())
			return;
		if(deslocamentoY<-imagem.getAltura())
			return;
		if(deslocamentoX>=pL)
			return;
		if(deslocamentoY>=pA)
			return;
		
		int novoX=0,novoY=0,novaLargura=imagem.getLargura(),novaAltura=imagem.getAltura();
		//Ajusta valores para não renderizar fora da tela
		if(deslocamentoX<0)
			novoX-=deslocamentoX;
		if(deslocamentoY<0)
			novoY-=deslocamentoY;
		if(novaLargura+deslocamentoX>=pL)
			novaLargura-=(novaLargura+deslocamentoX-pL);
		if(novaAltura+deslocamentoY>=pA)
			novaAltura-=(novaAltura+deslocamentoY-pA);
		
		int x,y;
		for(y=novoY;y<novaAltura;y++)
		{
				for(x=novoX;x<novaLargura;x++)
			{
					setPixel(x+deslocamentoX,y+deslocamentoY, imagem.getPixel()[x+y*imagem.getLargura()]);
					setBlocoLuz(x+deslocamentoX,y+deslocamentoY, imagem.getBlocoLuz(), imagem.getPixel()[x+y*imagem.getLargura()]);
			}
				
		}
		
		
	}
	
	public void desenharImagemTile(ImagemTile imagem, int deslocamentoX, int deslocamentoY, int tileX, int tileY)
	{
		if((imagem.isAlpha())&&(!processando))
		{
			pedidoI.add(new PedidoImagem(imagem.getTileImagem(tileX, tileY),profundidadeZ,deslocamentoX,deslocamentoY));
			return;
		}
		//Evita tentar renderizar fora da tela
		if(deslocamentoX<-imagem.getTileL())
			return;
		if(deslocamentoY<-imagem.getTileA())
			return;
		if(deslocamentoX>=pL)
			return;
		if(deslocamentoY>=pA)
			return;

		int novoX=0,novoY=0,novaLargura=imagem.getTileL(),novaAltura=imagem.getTileA();
		//Ajusta valores para não renderizar fora da tela
		if(deslocamentoX<0)
			novoX-=deslocamentoX;
		if(deslocamentoY<0)
			novoY-=deslocamentoY;
		if(novaLargura+deslocamentoX>=pL)
			novaLargura-=(novaLargura+deslocamentoX-pL);
		if(novaAltura+deslocamentoY>=pA)
			novaAltura-=(novaAltura+deslocamentoY-pA);

		int x,y;
		for(y=novoY;y<novaAltura;y++)
		{
			for(x=novoX;x<novaLargura;x++)
			{
				setPixel(x+deslocamentoX,y+deslocamentoY, imagem.getPixel()[(x+tileX*imagem.getTileL())+(y+tileY*imagem.getTileA())*imagem.getLargura()]);
				setBlocoLuz(x+deslocamentoX,y+deslocamentoY, imagem.getBlocoLuz(), imagem.getPixel()[(x+tileX*imagem.getTileL())+(y+tileY*imagem.getTileA())*imagem.getLargura()]);
			}
		}

	}
	
	public void drawnRect(int deslocamentoX, int deslocamentoY, int largura, int altura, int cor, boolean opaco)
	{
		//Evita tentar renderizar fora da tela
		if(deslocamentoX<-largura)
			return;
		if(deslocamentoY<-altura)
			return;
		if(deslocamentoX>=pL)
			return;
		if(deslocamentoY>=pA)
			return;

		int novoX=0,novoY=0,novaLargura=largura,novaAltura=altura;
		//Ajusta valores para não renderizar fora da tela
		if(deslocamentoX<0)
			novoX-=deslocamentoX;
		if(deslocamentoY<0)
			novoY-=deslocamentoY;
		if(novaLargura+deslocamentoX>=pL)
			novaLargura-=(novaLargura+deslocamentoX-pL);
		if(novaAltura+deslocamentoY>=pA)
			novaAltura-=(novaAltura+deslocamentoY-pA);
		
		int x,y,luz;
		
		if(opaco)
			luz=Luz.CHEIO;
		else
			luz=Luz.NADA;
		
		for(x=novoX;x<=novaLargura;x++)
		{
			setPixel(x+deslocamentoX,deslocamentoY,cor);
			setPixel(x+deslocamentoX,deslocamentoY+novaAltura,cor);
			setBlocoLuz(x+deslocamentoX,deslocamentoY,luz,cor);
			setBlocoLuz(x+deslocamentoX,deslocamentoY+novaAltura,luz,cor);

		}
		for(y=novoY;y<=novaAltura;y++)
		{
			setPixel(deslocamentoX,y+deslocamentoY,cor);
			setPixel(deslocamentoX+novaLargura,y+deslocamentoY,cor);
			setBlocoLuz(deslocamentoX,y+deslocamentoY,luz,cor);
			setBlocoLuz(deslocamentoX+novaLargura,y+deslocamentoY,luz,cor);
		}
	}
	
	public void drawnFillRect(int deslocamentoX, int deslocamentoY, int largura, int altura, int cor, boolean opaco)
	{
		//Evita tentar renderizar fora da tela
		if(deslocamentoX<-largura)
			return;
		if(deslocamentoY<-altura)
			return;
		if(deslocamentoX>=pL)
			return;
		if(deslocamentoY>=pA)
			return;

		int novoX=0,novoY=0,novaLargura=largura,novaAltura=altura;
		//Ajusta valores para não renderizar fora da tela
		if(deslocamentoX<0)
			novoX-=deslocamentoX;
		if(deslocamentoY<0)
			novoY-=deslocamentoY;
		if(novaLargura+deslocamentoX>=pL)
			novaLargura-=(novaLargura+deslocamentoX-pL);
		if(novaAltura+deslocamentoY>=pA)
			novaAltura-=(novaAltura+deslocamentoY-pA);
		int x,y,luz;

		if(opaco)
			luz=Luz.CHEIO;
		else
			luz=Luz.NADA;
		
		for(y=novoY;y<novaAltura;y++)
		{
			for(x=novoX;x<novaLargura;x++)
			{
				setPixel(x+deslocamentoX,y+deslocamentoY,cor);
				setBlocoLuz(x+deslocamentoX,y+deslocamentoY,luz,cor);
			}
		}
	}
	
	public void drawnCircle(int deslocamentoX, int deslocamentoY, int raio, int cor, boolean opaco)
	{
		int diametro = 2*raio;
		int x,y, luz;
		
		if(opaco)
			luz=Luz.CHEIO;
		else
			luz=Luz.NADA;
		
		for(y=0;y<diametro;y++)
		{
			for(x=0;x<diametro;x++)
			{
				double distancia=Math.sqrt((x-raio)*(x-raio)+(y-raio)*(y-raio));
				
				if((distancia<raio)&&(distancia>raio-(1.5)))
				{
					setPixel(x+deslocamentoX,y+deslocamentoY,cor);
					setBlocoLuz(x+deslocamentoX,y+deslocamentoY,luz,cor);
				}
			}
		}
	}
	
	public void drawnFillCircle(int deslocamentoX, int deslocamentoY, int raio, int cor, boolean opaco)
	{
		int diametro = 2*raio;
		int x,y,luz;
		
		if(opaco)
			luz=Luz.CHEIO;
		else
			luz=Luz.NADA;
		
		for(y=0;y<diametro;y++)
		{
			for(x=0;x<diametro;x++)
			{
				double distancia=Math.sqrt((x-raio)*(x-raio)+(y-raio)*(y-raio));
				
				if(distancia<raio)
				{
					setPixel(x+deslocamentoX,y+deslocamentoY,cor);
					setBlocoLuz(x+deslocamentoX,y+deslocamentoY,luz,cor);
				}
			}
		}
	}
	
	public void desenharLuz(Luz luz,int deslocamentoX, int deslocamentoY)
	{
		pedidoL.add(new PedidoLuz(luz,deslocamentoX,deslocamentoY));
	}
	
	private void desenharPedidoLuz(Luz luz,int deslocamentoX, int deslocamentoY)
	{
		int pixel_atual;
		for(pixel_atual=0;pixel_atual<=luz.getDiametro();pixel_atual++)
		{
			desenharLinhaLuz(luz,luz.getRaio(),luz.getRaio(),pixel_atual,0,deslocamentoX,deslocamentoY);
			desenharLinhaLuz(luz,luz.getRaio(),luz.getRaio(),pixel_atual,luz.getDiametro(),deslocamentoX,deslocamentoY);
			desenharLinhaLuz(luz,luz.getRaio(),luz.getRaio(),0,pixel_atual,deslocamentoX,deslocamentoY);
			desenharLinhaLuz(luz,luz.getRaio(),luz.getRaio(),luz.getDiametro(),pixel_atual,deslocamentoX,deslocamentoY);
		}
	}
	
	private void desenharLinhaLuz(Luz luz, int x0, int y0, int x1, int y1,int deslocamentoX, int deslocamentoY)
	{
		int dx,dy,sx,sy,err,err2;
		dx=Math.abs(x1-x0);
		dy=Math.abs(y1-y0);
		sx=x0 < x1 ? 1 : -1;
		sy=y0 < y1 ? 1 : -1;
		err=dx-dy;

		while(true)
		{
			int telaX,telaY,corLuz;
			telaX=x0-luz.getRaio()+deslocamentoX;
			telaY=y0-luz.getRaio()+deslocamentoY;
			if((telaX<0)||(telaX>=pL)||(telaY<0)||(telaY>=pA))
				return;
			corLuz=luz.getLuzValor(x0, y0);
			if(corLuz==0)
				return;
			if((blocoLuz[telaX+telaY*pL]==Luz.CHEIO))
				return;
			
			setMapaLuz(telaX,telaY,corLuz);
			if((x0==x1)&&(y0==y1))
				break;
			err2=2*err;
			if(err2>-1*dy)
			{
				err-=dy;
				x0+=sx;
			}
			if(err2<dx)
			{
				err+=dx;
				y0+=sy;
			}
		}
	}
	
	
	//CORES
	public static final int WHITE = 0xffffffff;
	public static final int BLACK = 0xff000000;
	public static final int RED = 0xffff0000;
	public static final int BLUE = 0xff0000ff;
	public static final int GREEN = 0xff00ff00;
	public static final int YELLOW = 0xffffff00;
	public static final int CYAN = 0xff00ffff;
	public static final int MAGENTA = 0xffff00ff;
	public static final int GRAY = 0xff646464;
	public static final int ORANGE = 0xffffb400;
	public static final int BROWN = 0xff473302;
	public static final int PURPLE = 0xff690bc9;
	
	public static final int ESCURO_6 = 0xff000000;
	public static final int ESCURO_5 = 0xff111111;
	public static final int ESCURO_4 = 0xff232323;
	public static final int ESCURO_3 = 0xff3e3e3e;
	public static final int ESCURO_2 = 0xff4d4d4d;
	public static final int ESCURO_1 = 0xff6b6b6b;
	public static final int CLARO = 0xffffffff;
	
	
	// |==============================================================================|
	// Metodos Gets
	
	public int getProfundidadeZ()
	{
		return profundidadeZ;
	}
	// |==============================================================================|
	// Metodos Sets
	
	public void setProfundidadeZ(int profundidadeZ)
	{
		this.profundidadeZ=profundidadeZ;
	}
	public void setEscuridao(int nivel)
	{
		corAmbiente=nivel;
	}
}
