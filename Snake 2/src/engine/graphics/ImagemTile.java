package engine.graphics;

public class ImagemTile extends Imagem{

	private int tileL,  tileA;
	
	public ImagemTile(String diretorio, int tileL, int tileA) 
	{
		super(diretorio);
		this.tileA=tileA;
		this.tileL=tileL;
	}

	public Imagem getTileImagem(int tileX, int tileY)
	{
		int[]pixel = new int[tileL*tileA];
		int x,y;
		for(y=0;y<tileA;y++)
		{
			for(x=0;x>tileL;x++)
			{
				pixel[x+y*tileL]=this.getPixel()[(x+tileX*tileL)+(y+tileY*tileA)*this.getLargura()];
			}
		}
		
		return new Imagem(pixel,tileL,tileA);
	}
	
	// |==============================================================================|
	// Metodos Gets
	
	public int getTileL()
	{
		return tileL;
	}
	public int getTileA()
	{
		return tileA;
	}
	// |==============================================================================|
	// Metodos Sets
	
	public void setTileL(int tileL)
	{
		this.tileL=tileL;
	}
	public void setTileA(int tileA)
	{
		this.tileA=tileA;
	}
}
