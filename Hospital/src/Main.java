/**
 * 병원 거리 최소화하기
 * 삼성 SW 역량테스트 2018 상반기 오후 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/min-of-hospital-distance/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 8. 23.
 * 
 * 백준 15686 치킨 배달
 * https://www.acmicpc.net/problem/15686
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
    
    static int n, m;
    static int[][] map; // 0 빈칸, 사람 1, 병원 2

    static ArrayList<int []> personList = new ArrayList<>(); // 사람 위치 저장
    static ArrayList<int []> hospitalList = new ArrayList<>(); // 병원 위치 저장

    static int result = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        map = new int[n][n];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());

                if (map[i][j] == 1) {
                    personList.add(new int[] {i, j});
                } else if (map[i][j] == 2) {
                    hospitalList.add(new int[] {i, j});
                }
            }
        }

        boolean[] visited = new boolean[hospitalList.size()]; // 특정 병원 방문했는지 확인

        // comb1(visited, 0, m);
        comb2(visited, 0, m);

        System.out.println(result);
    }

    // 조합 사용해서 m개의 병원 뽑음

    // 1. 재귀 사용
    private static void comb1(boolean[] visited, int depth, int r) {
        if (r == 0) {
            ArrayList<int[]> pick = new ArrayList<>();

            for (int i = 0; i < hospitalList.size(); i++) {
                if (visited[i]) {
                    pick.add(hospitalList.get(i));
                }
            }

            int distance = getDiatance(pick);
            result = Math.min(result, distance);

            return;
        }

        if (depth == hospitalList.size()) {
            return;
        }

        visited[depth] = true;
        comb1(visited, depth + 1, r - 1); // 병원 뽑은 경우

        visited[depth] = false;
        comb1(visited, depth + 1, r); // 병원 뽑지 않은 경우
    }

    // 2. 백트래킹 사용
    private static void comb2(boolean[] visited, int start, int r) {
        if (r == 0) {
            ArrayList<int[]> pick = new ArrayList<>();

            for (int i = 0; i < hospitalList.size(); i++) {
                if (visited[i]) {
                    pick.add(hospitalList.get(i));
                }
            }

            int distance = getDiatance(pick);
            result = Math.min(result, distance);

            return;
        }

        for (int i = start; i < hospitalList.size(); i++) {
            visited[i] = true;
            comb2(visited, i + 1, r - 1);
            visited[i] = false;
        }
    }

    // 병원과 사람 사이의 거리 계산
    private static int getDiatance(ArrayList<int[]> pick) {
        int sum = 0; // 

        for (int[] person : personList) {
            int min = Integer.MAX_VALUE;

            // 가장 가까운 병원 거리 찾음
            for (int[] hospital : pick) {
                int distance = Math.abs(person[0] - hospital[0]) + Math.abs(person[1] - hospital[1]);
                min = Math.min(min, distance);
            }

            sum += min;
        }

        return sum;
    }

}