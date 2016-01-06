public Poodle extends DogOverride{
	public Poodle(){
		super("Spot",5);
	}

	@Override
	public String bark(){
		return "I am a poodle!";
	}
}
