import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import static java.lang.System.out;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.CREATE;
import java.util.ArrayList;
import java.util.Scanner;
import static java.nio.file.StandardOpenOption.*;

public class ListMaker {
    static ArrayList<String> list = new ArrayList<>();
    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        String userCmd;
        boolean fileNeedSave = false;
        boolean fileOpened = false;
        String rec = "";

        do {
            displayMenu();
            userCmd = SafeInput.getRegExString(in, "Please Enter a command", "[AaIiDdMmOoSsCcQqVv]");
            try {
                switch (userCmd) {
                    case "o":
                    case "O":
                        if (fileNeedSave) {
                            String save = SafeInput.getRegExString(in, "You have unsaved changes. Save first? (Y/N)", "[YyNn]");
                            if (save.equalsIgnoreCase("Y")) SaveFile();
                        }
                        OpenFile();
                        fileOpened = true;
                        fileNeedSave = false;
                        break;
                    case "a":
                    case "A":
                        if (!fileOpened) {
                            System.out.println("Please open the file first!");
                        } else {
                            rec = SafeInput.getNonZeroLenString(in, "Please enter a new list item");
                            list.add(rec);
                            fileNeedSave = true;
                        }
                        break;
                    case "i":
                    case "I":
                        if (!fileOpened) break;
                        rec = SafeInput.getNonZeroLenString(in, "Enter item to insert");
                        int indexI = SafeInput.getRangeInt(in, "Insert at index", 0, list.size());
                        list.add(indexI, rec);
                        fileNeedSave = true;
                        break;
                    case "d":
                    case "D":
                        if (!fileOpened || list.size() == 0) break;
                        int indexD = SafeInput.getRangeInt(in, "Delete item at index", 0, list.size() - 1);
                        list.remove(indexD);
                        fileNeedSave = true;
                        break;
                    case "m":
                    case "M":
                        if (!fileOpened || list.size() < 2) break;
                        int from = SafeInput.getRangeInt(in, "Move item FROM index", 0, list.size() - 1);
                        int to = SafeInput.getRangeInt(in, "Move item TO index", 0, list.size() - 1);
                        String temp = list.remove(from);
                        list.add(to, temp);
                        fileNeedSave = true;
                        break;
                    case "c":
                    case "C":
                        if (!fileOpened) break;
                        String confirm = SafeInput.getRegExString(in, "Clear list? (Y/N)", "[YyNn]");
                        if (confirm.equalsIgnoreCase("Y")) {
                            list.clear();
                            fileNeedSave = true;
                        }
                        break;
                    case "s":
                    case "S":
                        if (!fileOpened) break;
                        SaveFile();
                        fileNeedSave = false;
                        break;
                    case "v":
                    case "V":
                        for (int i = 0; i < list.size(); i++) {
                            System.out.println(i + ": " + list.get(i));
                        }
                        break;
                    case "q":
                    case "Q":
                        if (fileNeedSave) {
                            String save = SafeInput.getRegExString(in, "You have unsaved changes. Save before quitting? (Y/N)", "[YyNn]");
                            if (save.equalsIgnoreCase("Y")) SaveFile();
                        }
                        running = false;
                        break;
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found!!!");
            } catch (IOException e) {
                System.out.println("I/O Error occurred");
            }
        } while (running);
    }

    private static void OpenFile() throws IOException {
        File workingDirectory = new File(System.getProperty("user.dir"));
        Path file = Paths.get(workingDirectory.getPath() + "\\src\\data.txt");
        InputStream in = new BufferedInputStream(Files.newInputStream(file, CREATE));
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        list.clear();
        int line = 0;
        String rec = "";
        while (reader.ready()) {
            rec = reader.readLine();
            list.add(rec);
            line++;
        }

        reader.close();
        System.out.println("\nData file read!");
    }

    private static void SaveFile() throws IOException {
        File workingDirectory = new File(System.getProperty("user.dir"));
        Path file = Paths.get(workingDirectory.getPath() + "\\src\\data.txt");
        OutputStream out = new BufferedOutputStream(Files.newOutputStream(file, CREATE));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

        for (String rec : list) {
            writer.write(rec, 0, rec.length());
            writer.newLine();
        }
        writer.close();
        System.out.println("Data file written!");
    }

    private static void displayMenu() {
        System.out.println("\n[A]dd [I]nsert [D]elete [M]ove [O]pen [S]ave [C]lear [V]iew [Q]uit");
    }
}
