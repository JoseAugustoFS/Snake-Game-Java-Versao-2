package engine;

public class EngineCore implements Runnable {

	private Thread thread;
	private Window window;
	private Renderizar renderizar;
	private Entrada entrada;
	private GameEngine game;
	
	private boolean funcionando = false;
	private final double TAXA_ATUALIZACAO = 1.0/60.0;
	private int largura=320, altura=240;
	private float escala=3f;
	private int fps;
	private String titulo = " ";
	
	public EngineCore(GameEngine game)
	{
		this.game=game;
	}
	
	public void start()
	{
		window = new Window(this);
		renderizar = new Renderizar(this);
		entrada = new Entrada(this);
		
		thread = new Thread(this);
		thread.run();
	}
	
	public void stop()
	{
		
	}
	
	public void run()
	{
		funcionando = true;
		boolean render = false;
		double tempoInicial = 0;
		double tempoFinal = System.nanoTime()/1000000000.0;
		double tempoPassado = 0;
		double tempoInvalido = 0;
		double frameTime = 0;
		int frames = 0;
		fps = 0;
		
		
		while(funcionando)
		{
			render = false;
			
			tempoInicial = System.nanoTime()/1000000000.0;
			tempoPassado = tempoInicial - tempoFinal;
			tempoFinal = tempoInicial;
			
			tempoInvalido += tempoPassado;
			frameTime += tempoPassado;
			
			while(tempoInvalido >= TAXA_ATUALIZACAO)
			{
				tempoInvalido -= TAXA_ATUALIZACAO;
				render=true;
				
				
				game.update(this, (float)TAXA_ATUALIZACAO);
				entrada.update();
				if(frameTime>= 1.0)
				{
					frameTime=0;
					fps=frames;
					frames=0;
					//System.out.println("FPS: "+fps);
				}
				
			}
			if(render)
			{
				renderizar.limpar();
				game.render(this, renderizar);
				renderizar.Processar();
				//renderizar.desenharTexto("FPS: "+fps, 0, 0, 0xffffff00);
				window.update();
				frames++;
			}
			else
			{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		dispose();
	}
	
	private void dispose()
	{
		
	}
	
	// |==============================================================================|
	// Metodos Gets
	
	public int getLargura()
	{
		return largura;
	}
	public int getAltura()
	{
		return altura;
	}
	public float getEscala()
	{
		return escala;
	}
	public String getTitulo()
	{
		return titulo;
	}
	public Window getWindow()
	{
		return window;
	}
	public Entrada getEntrada()
	{
		return entrada;
	}
	public int getFPS()
	{
		return fps;
	}
	// |==============================================================================|
	// Metodos Sets
	
	public void setLargura(int largura)
	{
		this.largura=largura;
	}
	public void setAltura(int altura)
	{
		this.altura=altura;
	}
	public void setEscala(float escala)
	{
		this.escala=escala;
	}
	public void setTitulo(String titulo)
	{
		this.titulo=titulo;
	}
}
