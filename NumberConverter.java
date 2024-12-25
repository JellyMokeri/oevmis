import java.util.Scanner;

public class NumberConverter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите исходную систему счисления (2-16): ");
        int sourceBase = 0;
        while (sourceBase < 2 || sourceBase > 16) {
            try {
                sourceBase = scanner.nextInt();
                if (sourceBase < 2 || sourceBase > 16) {
                    System.err.print("Ошибка: Некорректная исходная система счисления. Попробуйте снова: ");
                }
            } catch (java.util.InputMismatchException e) {
                System.err.print("Ошибка: Нецелое число. Попробуйте снова: ");
                scanner.next(); // Очистка неправильного ввода
            }
        }
        System.out.print("Введите конечную систему счисления (2-16): ");
        int destinationBase = 0;
        while (destinationBase < 2 || destinationBase > 16){
            try{
                destinationBase = scanner.nextInt();
                if (destinationBase < 2 || destinationBase > 16) {
                    System.err.print("Ошибка. Некорректная конечная система счисления. Попробуйте снова: ");
                }
            } catch (java.util.InputMismatchException e){
                System.err.print("Ошибка. Нецелое число. Попробуйте снова: ");
                scanner.next();
            }
        }
        System.out.print("Введите число в исходной системе счисления: ");
        String numberString = scanner.next();
        try {
            long decimalValue = convertFromBase(numberString, sourceBase);
            String result = convertToBase(decimalValue, destinationBase);
            if (destinationBase == 2) {
                String directCode = generateDirectCode(decimalValue);
                System.out.println("Результат: " + result + " и Прямой код " + directCode);
            } else {
                System.out.println("Результат: " + result);
            }
        } catch (IllegalArgumentException e) {
            System.err.print("Ошибка. " + e.getMessage() + " Попробуйте снова: ");
            scanner.next();
        }
        scanner.close();
    }

private static long convertFromBase(String numberString, int sourceBase) {
        long decimalValue = 0;
        int sign = 1;

        if (numberString.startsWith("-")) {
            sign = -1;
            numberString = numberString.substring(1);
        }

        // Critical Input Validation!
        if (sourceBase < 2 || sourceBase > 16) {
            throw new IllegalArgumentException("Invalid source base.");
        }

        for (int i = 0; i < numberString.length(); i++) {
            char digit = numberString.charAt(i);
            int digitValue;
            if (Character.isDigit(digit)) {
                digitValue = digit - '0';
            } else if (digit >= 'A' && digit <= 'F') {
                digitValue = digit - 'A' + 10;
            } else {
                throw new IllegalArgumentException("Лишние знаки в числе: " + digit);
            }
            if (digitValue >= sourceBase) {
                throw new IllegalArgumentException("Число больше системы счисления: " + digit);
            }
            decimalValue = decimalValue * sourceBase + digitValue;
        }

        return decimalValue * sign;
    }

    //метод для преобразования в другую СС
    private static String convertToBase(long decimalValue, int destinationBase) {
        if (decimalValue == 0) {
            return "0";
        }

        boolean isNegative = decimalValue < 0;
        decimalValue = Math.abs(decimalValue);

        StringBuilder result = new StringBuilder();
        while (decimalValue > 0) {
            int remainder = (int) (decimalValue % destinationBase);
            if (remainder < 10) {
                result.insert(0, remainder);
            } else {
                result.insert(0, (char) ('A' + remainder - 10));
            }
            decimalValue /= destinationBase;
        }
        if (isNegative) {
            result.insert(0, "-");
        }
        return result.toString();
    }

    private static String generateDirectCode(long decimalValue) {
        String binaryString = Long.toBinaryString(Math.abs(decimalValue));
        int bitSize = binaryString.length();
        int targetSize = calculateTargetBitSize(bitSize);

        StringBuilder directCode = new StringBuilder(binaryString);
        while (directCode.length() < targetSize) {
            directCode.insert(0, '0');
        }

        if (decimalValue < 0) {
            directCode.setCharAt(0, '1');
        }
        //Formatting directCode with spaces
        return formatBinaryString(directCode.toString());

    }

    private static int calculateTargetBitSize(int currentBitSize) {
        if (currentBitSize < 4) return 4;
        if (currentBitSize < 8) return 8;
        if (currentBitSize < 16) return 16;
        if (currentBitSize < 32) return 32;
        throw new IllegalArgumentException("Binary string is too large.");
    }

    private static String formatBinaryString(String binaryString) {
        StringBuilder formattedString = new StringBuilder();
        for(int i = 0; i < binaryString.length(); ++i) {
            formattedString.append(binaryString.charAt(i));
            if((i + 1) % 4 == 0 && i < binaryString.length() - 1){
                formattedString.append(' ');
            }
        }
        return formattedString.toString();
    }

}