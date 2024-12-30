import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ShamirSecretSharing {

    // Method to decode the value based on the base
    public static int decodeValue(String value, int base) {
        return Integer.parseInt(value, base);
    }

    // Method to perform Lagrange interpolation and find the constant term (c)
    public static double lagrangeInterpolation(List<int[]> points, int k) {
        double secret = 0.0;

        for (int i = 0; i < k; i++) {
            double xi = points.get(i)[0];
            double yi = points.get(i)[1];

            double term = yi;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    double xj = points.get(j)[0];
                    term *= -xj / (xi - xj);
                }
            }

            secret += term;
        }

        return secret;
    }

    public static void main(String[] args) {
        // File path of the JSON file
        String filePath = "testcase.json"; // Replace with the actual file path

        try {
            // Step 1: Read and parse the JSON manually
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line.trim());
            }
            reader.close();

            String json = jsonBuilder.toString();

            // Extract n and k
            int n = Integer.parseInt(json.split("\"n\":")[1].split(",")[0].trim());
            int k = Integer.parseInt(json.split("\"k\":")[1].split("}")[0].trim());

            // Extract points
            List<int[]> points = new ArrayList<>();

            String[] entries = json.split("},");
            for (String entry : entries) {
                if (entry.contains("\"base\"")) { // Check if the entry contains point data
                    int x = Integer.parseInt(entry.split("\"")[1].trim()); // Extract the key
                    int base = Integer.parseInt(entry.split("\"base\":")[1].split(",")[0].trim().replaceAll("\"", ""));
                    String value = entry.split("\"value\":")[1].replace("}", "").replace("\"", "").trim();
                    int y = decodeValue(value, base);

                    points.add(new int[]{x, y});
                }
            }

            // Step 2: Perform Lagrange interpolation to find the secret
            double secret = lagrangeInterpolation(points, k);

            // Step 3: Output the secret
            System.out.println("The secret (constant term) is: " + Math.round(secret));

        } catch (Exception e) {
            System.err.println("Error processing the file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
