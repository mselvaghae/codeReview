package com.mycompany.codeReview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for analyzing and checking password strength using clustering. This class ensures
 * only one instance is used throughout the application.
 */
public final class AwesomePasswordChecker {

  // Singleton instance
  private static AwesomePasswordChecker instance;

  // List of cluster centers for distance computation
  private final List<double[]> clusterCenters = new ArrayList<>();

  /**
   * Retrieves the singleton instance, initialized with the specified cluster center file.
   *
   * @param file The file containing cluster center data.
   * @return The singleton instance of {@link AwesomePasswordChecker}.
   * @throws IOException If an error occurs while reading the file.
   */
  public static AwesomePasswordChecker getInstance(final File file) throws IOException {
    if (instance == null) {
      instance = new AwesomePasswordChecker(new FileInputStream(file));
    }
    return instance;
  }

  /**
   * Retrieves the singleton instance, initialized with the default resource file.
   *
   * @return The singleton instance of {@link AwesomePasswordChecker}.
   * @throws IOException If an error occurs while reading the default resource file.
   */
  public static AwesomePasswordChecker getInstance() throws IOException {
    if (instance == null) {
      InputStream is =
          AwesomePasswordChecker.class
              .getClassLoader()
              .getResourceAsStream("cluster_centers_HAC_aff.csv");
      instance = new AwesomePasswordChecker(is);
    }
    return instance;
  }

  /**
   * Private constructor initializes the checker with cluster center data.
   *
   * @param is The input stream containing cluster center data.
   * @throws IOException If an error occurs while reading the input stream.
   */
  private AwesomePasswordChecker(final InputStream is) throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        double[] center = new double[values.length];
        for (int i = 0; i < values.length; ++i) {
          try {
            center[i] = Double.parseDouble(values[i].replace(",", "."));
          } catch (NumberFormatException e) {
            throw new IOException("Erreur de format dans le fichier CSV : " + values[i], e);
          }
        }
        clusterCenters.add(center);
      }
    }
  }

  /**
   * Generates a numerical mask for a given password based on character rules.
   *
   * @param password The password to analyze.
   * @return An integer array representing the character mask of the password.
   */
  public int[] maskAff(String password) {
    int limit = Math.min(password.length(), 28);
    int[] maskArray = new int[limit];

    for (int i = 0; i < limit; ++i) {
      char c = password.charAt(i);
      switch (c) {
        case 'e':
        case 's':
        case 'a':
        case 'i':
        case 't':
        case 'n':
        case 'r':
        case 'u':
        case 'o':
        case 'l':
          maskArray[i] = 1;
          break;
        case 'E':
        case 'S':
        case 'A':
        case 'I':
        case 'T':
        case 'N':
        case 'R':
        case 'U':
        case 'O':
        case 'L':
          maskArray[i] = 3;
          break;
        case '>':
        case '<':
        case '-':
        case '?':
        case '.':
        case '/':
        case '!':
        case '%':
        case '@':
        case '&':
          maskArray[i] = 6;
          break;
        default:
          if (Character.isLowerCase(c)) {
            maskArray[i] = 2;
          } else if (Character.isUpperCase(c)) {
            maskArray[i] = 4;
          } else if (Character.isDigit(c)) {
            maskArray[i] = 5;
          } else {
            maskArray[i] = 7;
          }
      }
    }
    return maskArray;
  }

  /**
   * Computes the minimum Euclidean distance between the password's mask and cluster centers.
   *
   * @param password The password to analyze.
   * @return The minimum distance as a {@code double}.
   */
  public double getDistance(final String password) {
    int[] maskArray = maskAff(password);
    double minDistance = Double.MAX_VALUE;

    for (double[] center : clusterCenters) {
      minDistance = Math.min(euclideanDistance(maskArray, center), minDistance);
    }
    return minDistance;
  }

  /**
   * Computes the Euclidean distance between two vectors.
   *
   * @param a The first vector (integer array).
   * @param b The second vector (double array).
   * @return The Euclidean distance as a {@code double}.
   */
  private double euclideanDistance(final int[] a, final double[] b) {
    double sum = 0;
    for (int i = 0; i < a.length; i++) {
      sum += (a[i] - b[i]) * (a[i] - b[i]);
    }
    return Math.sqrt(sum);
  }

  /**
   * Computes the MD5 hash of the input string using a custom implementation.
   *
   * @param input the string to hash
   * @return the MD5 hash as a hexadecimal string
   */
  public static String computeMd5(final String input) {
    try {
      final byte[] message = input.getBytes("UTF-8");
      final int messageLenBytes = message.length;
      final int numBlocks = ((messageLenBytes + 8) >>> 6) + 1;
      final int totalLen = numBlocks << 6;
      final byte[] paddingBytes = new byte[totalLen - messageLenBytes];
      paddingBytes[0] = (byte) 0x80;

      final long messageLenBits = (long) messageLenBytes << 3;
      final ByteBuffer lengthBuffer =
          ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN)
              .putLong(messageLenBits);
      final byte[] lengthBytes = lengthBuffer.array();

      final byte[] paddedMessage = new byte[totalLen];
      System.arraycopy(message, 0, paddedMessage, 0, messageLenBytes);
      System.arraycopy(paddingBytes, 0, paddedMessage, messageLenBytes, 
          paddingBytes.length);
      System.arraycopy(lengthBytes, 0, paddedMessage, totalLen - 8, 8);

      final int[] h = {0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476};

      // Step 6: Initialize constants
      final int[] k = {
        0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee, 0xf57c0faf, 0x4787c62a, 
        0xa8304613, 0xfd469501,
        0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be, 0x6b901122, 0xfd987193, 
        0xa679438e,0x49b40821,
        0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa, 0xd62f105d, 0x02441453, 
        0xd8a1e681,0xe7d3fbc8,
        0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed, 0xa9e3e905, 0xfcefa3f8, 0x676f02d9,
            0x8d2a4c8a,
        0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c, 0xa4beea44, 0x4bdecfa9, 0xf6bb4b60,
            0xbebfbc70,
        0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05, 0xd9d4d039, 0xe6db99e5, 0x1fa27cf8,
            0xc4ac5665,
        0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039, 0x655b59c3, 0x8f0ccc92, 0xffeff47d,
            0x85845dd1,
        0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1, 0xf7537e82, 0xbd3af235, 0x2ad7d2bb,
            0xeb86d391
      };

      final int[] r = {
        7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
        5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20,
        4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
        6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
      };

      // Step 7: Process message in 512-bit chunks
      for (int i = 0; i < numBlocks; i++) {
        final int[] w = new int[16];
        for (int j = 0; j < 16; j++) {
          w[j] =
              ByteBuffer.wrap(paddedMessage, (i << 6) + (j << 2), 4)
                  .order(ByteOrder.LITTLE_ENDIAN)
                  .getInt();
        }

        int a = h[0];
        int b = h[1];
        int c = h[2];
        int d = h[3];

        for (int j = 0; j < 64; j++) {
          final int f;
          final int g;
          if (j < 16) {
            f = (b & c) | (~b & d);
            g = j;
          } else if (j < 32) {
            f = (d & b) | (~d & c);
            g = (5 * j + 1) % 16;
          } else if (j < 48) {
            f = b ^ c ^ d;
            g = (3 * j + 5) % 16;
          } else {
            f = c ^ (b | ~d);
            g = (7 * j) % 16;
          }
          final int temp = d;
          d = c;
          c = b;
          b = b + Integer.rotateLeft(a + f + k[j] + w[g], r[j]);
          a = temp;
        }

        h[0] += a;
        h[1] += b;
        h[2] += c;
        h[3] += d;
      }

      // Step 8: Create the output hash
      final ByteBuffer md5Buffer = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN);
      md5Buffer.putInt(h[0]).putInt(h[1]).putInt(h[2]).putInt(h[3]);
      final byte[] md5Bytes = md5Buffer.array();

      final StringBuilder md5Hex = new StringBuilder();
      for (byte b : md5Bytes) {
        md5Hex.append(String.format("%02x", b));
      }

      return md5Hex.toString();

    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("UTF-8 not supported", e);
    }
  }

  /**
   * Main method for testing the functionality of {@link AwesomePasswordChecker}.
   *
   * @param args Command-line arguments.
   */
  public static void main(final String[] args) {
    // Example usage or testing logic here
  }
}
