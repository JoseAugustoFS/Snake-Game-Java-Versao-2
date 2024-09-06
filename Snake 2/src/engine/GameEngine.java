package engine;

public abstract class GameEngine {
	
	public abstract void update(EngineCore nucleo, float deltaTime);
	public abstract void render(EngineCore nucleo, Renderizar renderizar);
	
}
