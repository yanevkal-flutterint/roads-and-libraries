package org.kaloyan;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class MainSolution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int q = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, q).forEach(qItr -> {
            try {
                String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

                int n = Integer.parseInt(firstMultipleInput[0]);

                int m = Integer.parseInt(firstMultipleInput[1]);

                int c_lib = Integer.parseInt(firstMultipleInput[2]);

                int c_road = Integer.parseInt(firstMultipleInput[3]);

                List<List<Integer>> cities = new ArrayList<>();

                IntStream.range(0, m).forEach(i -> {
                    try {
                        cities.add(
                                Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                        .map(Integer::parseInt)
                                        .collect(toList())
                        );
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                long result = roadsAndLibraries(n, c_lib, c_road, cities);
                System.out.println(result);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
    }

    // n stands for numberOfCities
    public static long roadsAndLibraries(int n, int libraryCost, int roadCost, List<List<Integer>> potentialRoads) {

        // First we check if building a library cost less than building a road. In such case it's cheaper to build a library in every city.
        if (libraryCost <= roadCost) {
            return (long) n * libraryCost;
        }

        List<List<Integer>> allCitiesAndPotentialRoads = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            allCitiesAndPotentialRoads.add(new ArrayList<>());
        }

        for (List<Integer> road : potentialRoads) {
            allCitiesAndPotentialRoads.get(road.get(0)).add(road.get(1));
            allCitiesAndPotentialRoads.get(road.get(1)).add(road.get(0));
        }

        boolean[] visited = new boolean[n + 1];
        long totalCost = 0;

        for (int i = 1; i <= n; i++) {
            if (!visited[i]) {
                long citiesInGroup = dfs(i, allCitiesAndPotentialRoads, visited);
                totalCost += libraryCost + ((citiesInGroup - 1) * roadCost);
            }
        }

        return totalCost;
    }

    public static long dfs(int node, List<List<Integer>> cities, boolean[] visited) {
        Stack<Integer> stack = new Stack<>();
        stack.push(node);
        visited[node] = true;
        long size = 0;

        while (!stack.isEmpty()) {
            int currentCity = stack.pop();
            size++;

            for (int neighborCity : cities.get(currentCity)) {
                if (!visited[neighborCity]) {
                    visited[neighborCity] = true;
                    stack.push(neighborCity);
                }
            }
        }

        return size;
    }
}