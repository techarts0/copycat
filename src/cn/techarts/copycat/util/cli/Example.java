package cn.techarts.copycat.util.cli;

public class Example {
	public static void main(String[] args) {
		var cli = new CLI(new Command());
		cli.start("--Demo--", ">");
	}
}

class Command{
	@Action(name="help")
	public void help(String line) {
		System.out.println("This is a CLI demo.");
	}

	@Action(name="exit")
	public void exit(String line) {
		System.out.println("bye");
		System.exit(0);
	}
}
