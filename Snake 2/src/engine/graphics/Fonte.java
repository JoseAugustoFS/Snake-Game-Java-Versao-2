package engine.graphics;

public class Fonte {

	public static final Fonte PADRAO = new Fonte("/Fontes/FontePadrao12.png");
	public static final Fonte COMIC_SANS_12 = new Fonte("/Fontes/comicsans12.png");
	public static final Fonte ARIAL_11 = new Fonte("/Fontes/arial11.png");
	
	private Imagem ImagemFonte;
	private int[] deslocamentos;
	private int [] larguras;
	
	public Fonte(String diretorio)
	{
		ImagemFonte = new Imagem(diretorio);
		
		deslocamentos= new int[256];
		larguras= new int[256];
		
		int pixel_atual,unicode=0;
		for(pixel_atual=0;pixel_atual<ImagemFonte.getLargura();pixel_atual++)
		{
			if(ImagemFonte.getPixel()[pixel_atual]==0xff0000ff)
			{
				deslocamentos[unicode]=pixel_atual;
			}
			if(ImagemFonte.getPixel()[pixel_atual]==0xffffff00)
			{
				larguras[unicode]=pixel_atual-deslocamentos[unicode];
				unicode++;
			}
		}
	}
	// |==============================================================================|
	// Metodos Gets
	
	public Imagem getImagemFonte()
	{
		return ImagemFonte;
	}
	public int[] getDeslocamentos()
	{
		return deslocamentos;
	}
	public int[] getLarguras()
	{
		return larguras;
	}
	// |==============================================================================|
	// Metodos Sets
	
	public void setImagemFonte(Imagem ImagemFonte)
	{
		this.ImagemFonte=ImagemFonte;
	}
	public void setDeslocamentos(int[] deslocamentos)
	{
		this.deslocamentos=deslocamentos;
	}
	public void setLarguras(int[] larguras)
	{
		this.larguras=larguras;
	}
}
