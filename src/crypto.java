import java.util.*;

public class crypto {
    public static void main(String[] args){
        helloWhatToDo();
        System.out.println("Thanks for using Crypto, have a nice day!");
    }
    
    public static void helloWhatToDo(){
        Scanner input = new Scanner(System.in);
        System.out.println("Hello! Welcome to Crypto.");
        String c = "Y";//assuming the program will be run at least once.
        String a;
        while(c.equalsIgnoreCase("Y")){
            System.out.println("Do you want to encrypt something, or decrypt an information?");
            System.out.print("(E)ncrypt or (D)ecrypt ");
            a = input.next();
            if(a.equalsIgnoreCase("E"))
                initiateEncryption();
            else if (a.equalsIgnoreCase("D"))
                initiateDecryption();
            else
                printMessageError();
            System.out.println("Would you like Crypto to perform another task? (Y)es or (N)o");
            c = input.next();
            while(!c.equalsIgnoreCase("Y") && !c.equalsIgnoreCase("N")) printMessageError();
        }
    }

    public static void printMessageError(){
        System.out.println("Sorry, wrong input.");
    }

    public static String normalizeText(String text){
        String pMarks = " .,:;'\"!?()";
        for(int i=0; i<pMarks.length(); i++) text = deletePunctuation(text, pMarks.substring(i, i+1));
        text = text.toUpperCase();
        return text;
    }

    public static String deletePunctuation(String text, String c){
        while(text.contains(c)){
            int i = text.indexOf(c);
            text = text.substring(0, i) + text.substring(i+1, text.length());
        }
        return text;
    }

    public static String shiftAlphabet(int shift) {
        int start = 0;
        if (shift < 0) start = (int) 'Z' + shift + 1;
        else start = 'A' + shift;
        String result = "";
        char currChar = (char) start;
        for(; currChar <= 'Z'; ++currChar) result = result + currChar;
        if(result.length() < 26) for(currChar = 'A'; result.length() < 26; ++currChar) result = result + currChar;
        return result;
    }

        public static void initiateEncryption(){
        Scanner input = new Scanner(System.in);
        int key = 0;
        int letters = 0;
        System.out.println("What would you like to encrypt? ");
        String text = input.nextLine();
        System.out.print("Great! Now, would you like me to use (R)andom parameters or do you want to set them (Y)ourself? ");
        String a = input.next();
        if(a.equalsIgnoreCase("R")){//Running the program with random parameters;
            Random rand = new Random();
            key = rand.nextInt();
            while(letters == 0) letters = rand.nextInt(9);
            System.out.println("Do you want to know what parameters were chosen? (Y)es or (N)o ");
            String b = input.next();
            if(b.equalsIgnoreCase("Y")) System.out.println("Value used for encryption: " + key + ", your text will be grouped in " + letters + "s.");
            else if(!b.equalsIgnoreCase("N")) printMessageError();
        } else if (a.equalsIgnoreCase("Y")){
            System.out.print("Parameter for caesarify: ");
            key = input.nextInt();
            System.out.print("Parameter for groupify: ");
            letters = input.nextInt();
        } else printMessageError();
        System.out.println("Encryption initialized...");
        text = normalizeText(text);
        text = obify(text);
        text = caesarify(text, key);
        text = groupify(text, letters);
        System.out.println("Here's your text encrypted: ");
        System.out.println(text);
    }

    public static String groupify(String text, int letters){
        int i = 0;
        String result = "";
        for(; i<text.length(); i++){
            if(i%letters==0) result += " ";
            result += text.substring(i, i+1);
        }
        while(i%letters != 0){
            result += "x";
            i++;
        }
        result = result.trim();
        return result;
    }

    public static String obify(String text){
        String result = "";
        String vowels = "AEIOUY";
        for(int i=0; i<text.length();i++){
            if(vowels.contains(text.subSequence(i, i+1))) result += "OB";
            result += text.charAt(i);
        }
        return result;
    }

    public static String caesarify(String text, int key){
        String alph = shiftAlphabet(key); //We're getting our "new alphabet"
        String result = ""; //blank start of our new sentence
        for(int i=0; i<text.length(); i++){
            char temp = text.charAt(i); //we take current character to work with
            int j = alph.indexOf(temp); //we're looking for its place in the alphabet
            result+=alph.charAt(((j+key)%26 + 26)%26); //shifting our character by "key" places, +26 and % added so there's no problem with negative indexes and so it wraps nicely between Z and A.
        }
        return result;
    }

    public static void initiateDecryption(){
        Scanner input = new Scanner(System.in);
        System.out.println("What would you like to decrypt? ");
        String text = input.nextLine();
        System.out.print("Great! What shift parameter did you use for encryption? ");
        int key = input.nextInt();
        System.out.println("Decryption initialized...");
        text = degroupify(text);
        text = caesarify(text, -1*key);//this step needs no new method, since decrypting with key is equivalent to encrypting with -key.
        text = deobify(text);
        System.out.println("Here's your text decrypted: ");
        System.out.println(text);
    }

    public static String deobify(String text){
        String result = "";
        text = text.replaceAll("OBO","-");//remove all instances of a special case, where 'OB' were put before 'O', otherwise it generates rubbish - i replace this sequence with "-" temporarily, so it doesn't interfere with the rest
        for(int i=0; i<text.length(); i++){
            if(i>=1 && i<text.length()-1 && (text.charAt(i)=='O' && text.charAt(i+1)=='B') || (text.charAt(i)=='B' && text.charAt(i-1)=='O')) continue;
            else if(i==0 && (text.charAt(i)=='O' && text.charAt(i+1)=='B')) continue; //when the text that was encrypted starts with a vowel, it has this 'OB' fragment right and the beginning, so initial 'O' survives - this part gets rid of this bug.
            else result += text.charAt(i);
        }
        result = result.replace('-','O');//...and here finally 'OBO' became 'O', as it should.
        return result;
    }

    public static String degroupify(String text){
        text = deletePunctuation(text, " ");
        text = text.replace('x', ' '); //removing tail of x's, we get teil of white spaces instead
        return text.trim();//getting rid of the whole tail.
    }

}
