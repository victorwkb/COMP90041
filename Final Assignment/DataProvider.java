/*
 * Student name: Kai Bing Goh
 * Student ID: 981436
 * LMS username: kaibingg
 */

import java.io.*;
import java.util.ArrayList;

/**
 * Class in charge of unloading the input files into arraylist of data to be used throughout the
 * program, class also in charge of outputting into a new csv file via updateFile method
 */
public class DataProvider {
    ArrayList<Member> members = new ArrayList<Member>();
    ArrayList<Bill> bills = new ArrayList<Bill>();
    ArrayList<String> noMemIdBillList = new ArrayList<String>();

    /**
     * @param memberFile A path to the member file (e.g., members.csv)
     * @param billFile   A path to the bill file (e.g., bills.csv)
     * @throws DataAccessException If a file cannot be opened/read
     * @throws DataFormatException If the format of the the content is incorrect
     */
    public DataProvider(String memberFile, String billFile)
            throws DataAccessException, DataFormatException {
        BufferedReader inputStream;
        try {
//          FileInputStream memFileInputStream = new FileInputStream(memberFile);
            inputStream = new BufferedReader(new FileReader(memberFile));
            String inLine;
            String str1, str2, str3;
            // scan line by line, str1 = column1 etc
            while ((inLine = inputStream.readLine()) != null) {
                Member newMem = new Member();
                String[] memData = inLine.split(",");
                if (memData.length != 3) {
                    throw new DataFormatException("Wrong format in member file (no. of column)");
                }
                str1 = memData[0];
                str2 = memData[1];
                str3 = memData[2];
                // check member id format (break the loop for this line if wrong format)
                if (!isSixDigit(str1)) {
                    throw new DataFormatException("Wrong member id format");
                }
                // check name format (break the loop for this line if wrong format)
                if (!isWord(str2)) {
                    throw new DataFormatException("Wrong name format");
                }
                // check email format (break the loop for this line if wrong format)
                if (!emailIsValid(str3)) {
                    throw new DataFormatException("Wrong email format");
                }
                // add scanned info to object
                newMem.setIdNumber(str1);
                newMem.setName(str2);
                newMem.setEmail(str3);
                // add to list
                members.add(newMem);
            }
        } catch (FileNotFoundException e) {
            throw new DataAccessException("Fail to open member file, file not found");
        } catch (IOException e) {
            throw new DataAccessException("Error reading, writing or searching member file");
        }
        // Read bill file
        try {
            inputStream = new BufferedReader(new FileReader(billFile));
            String inLine;
            String str1, str2, str3, str4;
            // scan line by line, str1 = column 1 etc
            while ((inLine = inputStream.readLine()) != null) {
                Bill newBill = new Bill();
                String[] billData = inLine.split(",");
                if (billData.length != 4) {
                    throw new DataFormatException("Wrong format in bill file (no. of column)");
                }
                str1 = billData[0];
                str2 = billData[1];
                str3 = billData[2];
                str4 = billData[3];
                // check bill id format (break the loop for this line if wrong format)
                if (!isSixDigit(str1)) {
                    throw new DataFormatException("Wrong member id format");
                }
                //check bill amount format (break the loop for this line if wrong format)
                try {
                    double d = Double.parseDouble(str3);
                } catch (NumberFormatException e) {
                    throw new DataFormatException("Wrong bill amount format");
                }
                //check status if applied or not
                try {
                    newBill.checkStatusFormat(str4);
                } catch (IllegalArgumentException e) {
                    throw new DataFormatException("Wrong bill status boolean format");
                }
                // if member ID not in right format, assume no member ID
                if (!isSixDigit(str2)) {
                    newBill.setMemIdNum(null);
                    // for checking bill exist or not
                    noMemIdBillList.add(str1);
                } else {
                    // add scanned info to object
                    newBill.setMemIdNum(str2);
                }
                newBill.setBillIdNum(str1);
                newBill.setAmount(str3);
                newBill.setBillStatus(str4);
                bills.add(newBill);
            }
        } catch (FileNotFoundException e) {
            throw new DataAccessException("Fail to open bill file, file not found");
        } catch (IOException e) {
            throw new DataAccessException("Error reading, writing or searching bill file");
        }
    }

    // Method to check if email is valid when reading file using a regular expression
    public boolean emailIsValid(String email) {
        String regExp = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regExp);
    }

    // Member and Bill getter classes (from the arraylist)

    public Member getMember(String memId) {
        for (Member index : members) {
            if (memId.equalsIgnoreCase(index.getIdNumber())) {
                return index;
            }
        }
        return null;
    }

    public Bill getBill(String billId) {
        for (Bill index : bills) {
            if (billId.equalsIgnoreCase(index.getBillIdNum())) {
                return index;
            }
        }
        return null;
    }

    // Method to check if bill exists
    public boolean billExists(String billId) {
        for (Bill index : bills) {
            if (index.getBillIdNum().equalsIgnoreCase(billId)) {
                return true;
            }
        }
        return false;
    }

    // Method to check if bill doesn't have a member ID
    public boolean billNoMemId(String billId) {
        for (String index : noMemIdBillList) {
            if (index.equalsIgnoreCase(billId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to write to a file with the name used when accessing the bill file
     *
     * @param billFile The accessed bill file to be updated
     */
    public void updateFile(String billFile) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream(billFile));
        } catch (FileNotFoundException e) {
            System.out.println("Member bills file not found");
            System.exit(0);
        }
        for (Bill line : bills) {
            if (line.getMemIdNum() == null) {
                writer.printf("%s,,%s,%s\n", line.getBillIdNum(),
                        line.getAmount(), line.getBillStatus());
            } else {
                writer.printf("%s,%s,%s,%s\n", line.getBillIdNum(), line.getMemIdNum(),
                        line.getAmount(), line.getBillStatus());
            }
        }
        writer.flush();
        writer.close();
    }

    // Method to check if the String is strictly 6 digit
    public boolean isSixDigit(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return str.length() == 6;
    }

    // Check if a string is purely letters
    public boolean isWord(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isLetter(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
