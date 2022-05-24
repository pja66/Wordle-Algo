import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class main {
  public static void main(String[] args) throws IOException {

    // Starting List of 5 Letter Words
    List<String> listOfWords = importWords();

    // Number of words that can be tried
    for (int rounds = 0; rounds < 5; rounds++) {
      System.out.println(listOfWords.size());
      System.out.println(bestWord(listOfWords)[0]);
      System.out.println(bestWord(listOfWords)[1]);

      int[] grey = new int[5];
      int[] green = new int[5];
      int[] yellow = new int[5];

      // Current Word being Anyalized
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Enter Current Word");
      String currentWord = br.readLine();

      // Records Information being Returned
      for (int i = 0; i < 5; i++) {
        System.out.println("Enter Color for letter index " + i);
        String str = br.readLine();

        if (str.equals("grey")) {
          grey[i] = 1;
        } else if (str.equals("green")) {
          green[i] = 1;
        } else {
          yellow[i] = 1;
        }
      }

      int[][] informationMatrix = { grey, green, yellow };
      listOfWords = reduceList(listOfWords, informationMatrix, currentWord);
    }
  }

  // Returns best word option
  private static String[] bestWord(List<String> listOfWords) {
    String bestWord = "";
    double bestWordScore = -1;

    int[][] informationMatrix = new int[3][5];

    for (int i = 0; i < listOfWords.size(); i++) {
      String curWord = listOfWords.get(i);

      for (int a = 0; a < 3; a++) {
        for (int b = 0; b < 3; b++) {
          for (int c = 0; c < 3; c++) {
            for (int d = 0; d < 3; d++) {
              for (int e = 0; e < 3; e++) {

                informationMatrix[a][0] = 1;
                informationMatrix[b][1] = 1;
                informationMatrix[c][2] = 1;
                informationMatrix[d][3] = 1;
                informationMatrix[e][4] = 1;

                double curScore = reduceList(listOfWords, informationMatrix, curWord).size();
                double gainedInfromation = Math.log(listOfWords.size() / curScore);
                double probabibilty = curScore / listOfWords.size();
                double potentialScore = probabibilty * gainedInfromation;

                if (potentialScore > bestWordScore) {
                  bestWord = curWord;
                  bestWordScore = potentialScore;
                }

                informationMatrix[a][0] = 0;
                informationMatrix[b][1] = 0;
                informationMatrix[c][2] = 0;
                informationMatrix[d][3] = 0;
                informationMatrix[e][4] = 0;
              }
            }
          }
        }
      }
    }
    String[] val = { bestWord, String.valueOf(bestWordScore) };
    return val;
  }

  // Reducing the list given a word and information
  private static List<String> reduceList(List<String> listOfWords, int[][] informationMatrix, String currentWord) {
    List<String> tempListOfWords = new ArrayList<>(listOfWords);

    // The given result from the word
    int[] grey = informationMatrix[0];
    int[] green = informationMatrix[1];
    int[] yellow = informationMatrix[2];

    for (String tempword : listOfWords) {
      for (int j = 0; j < 5; j++) {

        // NEED TO FIX DOUBLE SAME LETTERS, WHEN ONE IS GREEN AND ONE IS YELLOW

        if (grey[j] == 1) {
          if (tempword.indexOf(currentWord.substring(j, j + 1)) != -1) {
            tempListOfWords.remove(tempword);
            break;
          }
        }

        if (green[j] == 1) {
          if (tempword.charAt(j) != currentWord.charAt(j)) {
            tempListOfWords.remove(tempword);
            break;
          }
        }

        if (yellow[j] == 1) {
          if (tempword.charAt(j) == currentWord.charAt(j)) {
            tempListOfWords.remove(tempword);
            break;
          }
        }
      }
    }

    return tempListOfWords;
  }

  // Import List of Five Letter Words
  private static List<String> importWords() throws IOException {
    List<String> listOfStrings = new ArrayList<String>();
    BufferedReader bf = new BufferedReader(new FileReader("wordsShort.txt"));
    String line = bf.readLine();

    while (line != null) {
      listOfStrings.add(line);
      line = bf.readLine();
    }
    bf.close();

    return listOfStrings;
  }
}