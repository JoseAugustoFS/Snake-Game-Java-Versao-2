package game;

public class Cobra {

	public static final int CIMA=0,BAIXO=1,DIREITA=2,ESQUERDA=3;
	
	private int xCabeca,yCabeca,PosCabeca1,PosCabeca2,PosCauda1,PosCauda2,tamanho;
	int[] xCorpo = new int[27];
	int[] yCorpo = new int[27];
	int[] PosCorpo1 = new int[27];
	int[] PosCorpo2 = new int[27];
	int[] CorpoDirecao = new int[27];
	
	public Cobra()
	{
		xCabeca=11;
		yCabeca=7;
		PosCabeca1=1;
		PosCabeca2=1;
		PosCauda1=1;
		PosCauda2=1;
		tamanho=5;
		int local_atual;
		for(local_atual=0;local_atual<=25;local_atual++)
		{
			xCorpo[local_atual]=-30;
			yCorpo[local_atual]=-30;
			CorpoDirecao[local_atual]=DIREITA;
			PosCorpo1[local_atual]=0;
			PosCorpo2[local_atual]=1;
		}
		xCorpo[1]=10;
		yCorpo[1]=7;
		xCorpo[2]=9;
		yCorpo[2]=7;
		xCorpo[3]=8;
		yCorpo[3]=7;
		xCorpo[4]=7;
		yCorpo[4]=7;
		xCorpo[5]=6;
		yCorpo[5]=7;
	}
	
	public void DefinirCabeca(int direcao)
	{
		switch(direcao)
		{
		case CIMA:
			PosCabeca1=0;
			PosCabeca2=0;
			yCabeca--;
			break;
		case BAIXO:
			PosCabeca1=1;
			PosCabeca2=0;
			yCabeca++;
			break;
		case DIREITA:
			PosCabeca1=1;
			PosCabeca2=1;
			xCabeca++;
			break;
		case ESQUERDA:
			PosCabeca1=0;
			PosCabeca2=1;
			xCabeca--;
			break;
		}
	}
	
	public void DefinirCorpo(int direcao)
	{
		xCorpo[0]=xCabeca;
		yCorpo[0]=yCabeca;
		CorpoDirecao[0]=direcao;
		int local_atual;
		for(local_atual=tamanho;local_atual>0;local_atual--)
		{
			xCorpo[local_atual]=xCorpo[local_atual-1];
			yCorpo[local_atual]=yCorpo[local_atual-1];
			CorpoDirecao[local_atual]=CorpoDirecao[local_atual-1];
			switch(CorpoDirecao[local_atual])
			{
			case CIMA:
				if(CorpoDirecao[local_atual+1]==CIMA)
				{
					PosCorpo1[local_atual]=0;						
					PosCorpo2[local_atual]=0;
				}
				else
				if(CorpoDirecao[local_atual+1]==ESQUERDA)
				{
					PosCorpo1[local_atual]=2;						
					PosCorpo2[local_atual]=0;
				}
				else
				if(CorpoDirecao[local_atual+1]==DIREITA)
				{
					PosCorpo1[local_atual]=1;						
					PosCorpo2[local_atual]=0;
				}
				if(local_atual==tamanho)
				{						
					PosCauda1=0;
					PosCauda2=0;
				}
				break;
			case BAIXO:
				if(CorpoDirecao[local_atual+1]==BAIXO)
				{
					PosCorpo1[local_atual]=0;						
					PosCorpo2[local_atual]=0;
				}
				else
				if(CorpoDirecao[local_atual+1]==ESQUERDA)
				{
					PosCorpo1[local_atual]=2;						
					PosCorpo2[local_atual]=1;
				}
				else
				if(CorpoDirecao[local_atual+1]==DIREITA)
				{
					PosCorpo1[local_atual]=1;						
					PosCorpo2[local_atual]=1;
				}
				if(local_atual==tamanho)
				{						
					PosCauda1=0;
					PosCauda2=1;
				}
				break;
			case DIREITA:
				if(CorpoDirecao[local_atual+1]==DIREITA)
				{
					PosCorpo1[local_atual]=0;						
					PosCorpo2[local_atual]=1;
				}
				else
				if(CorpoDirecao[local_atual+1]==CIMA)
				{
					PosCorpo1[local_atual]=2;						
					PosCorpo2[local_atual]=1;
				}
				else
				if(CorpoDirecao[local_atual+1]==BAIXO)
				{
					PosCorpo1[local_atual]=2;						
					PosCorpo2[local_atual]=0;
				}
				if(local_atual==tamanho)
				{						
					PosCauda1=1;
					PosCauda2=1;
				}
				break;
			case ESQUERDA:
				if(CorpoDirecao[local_atual+1]==ESQUERDA)
				{
					PosCorpo1[local_atual]=0;						
					PosCorpo2[local_atual]=1;
				}
				else
				if(CorpoDirecao[local_atual+1]==CIMA)
				{
					PosCorpo1[local_atual]=1;						
					PosCorpo2[local_atual]=1;
				}
				else
				if(CorpoDirecao[local_atual+1]==BAIXO)
				{
					PosCorpo1[local_atual]=1;						
					PosCorpo2[local_atual]=0;
				}
				if(local_atual==tamanho)
				{						
					PosCauda1=1;
					PosCauda2=0;
				}
				break;
			}
				
		}
	}

	public int getXCabeca()
	{
		return xCabeca;
	}
	public int getYCabeca()
	{
		return yCabeca;
	}
	public int getPosCabeca1()
	{
		return PosCabeca1;
	}
	public int getPosCabeca2()
	{
		return PosCabeca2;
	}
	public int[] getXCorpo()
	{
		return xCorpo;
	}
	public int[] getYCorpo()
	{
		return yCorpo;
	}
	public int[] getPosCorpo1()
	{
		return PosCorpo1;
	}
	public int[] getPosCorpo2()
	{
		return PosCorpo2;
	}
	public int getPosCauda1()
	{
		return PosCauda1;
	}
	public int getPosCauda2()
	{
		return PosCauda2;
	}
	public int getTamanho()
	{
		return tamanho;
	}
	
	
	public void setTamanho(int tamanho)
	{
		this.tamanho=tamanho;
	}
}
