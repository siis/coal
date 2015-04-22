package edu.psu.cse.siis.coal;
import edu.psu.cse.siis.coal.DefaultAnalysis;
import edu.psu.cse.siis.coal.DefaultCommandLineArguments;
import edu.psu.cse.siis.coal.DefaultCommandLineParser;



public class Main {
  public static void main(String[] args) {
    DefaultAnalysis<DefaultCommandLineArguments> analysis = new DefaultAnalysis<>();
    DefaultCommandLineParser parser = new DefaultCommandLineParser();
    DefaultCommandLineArguments commandLineArguments =
        parser.parseCommandLine(args, DefaultCommandLineArguments.class);
    if (commandLineArguments == null) {
      return;
    }
    analysis.performAnalysis(commandLineArguments);
  }
}
