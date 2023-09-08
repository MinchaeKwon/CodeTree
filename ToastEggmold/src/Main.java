/**
 * 토스트 계란틀
 * 삼성 SW 역량테스트 2018 하반기 오전 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/toast-eggmold/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 8.
 * 
 * 백준 16234 인구 이동
 * https://www.acmicpc.net/problem/16234
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    // 상하좌우
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};

    static int n;
    static int L, R; // 계란 이동 범위의 최솟값, 최댓값
    static int[][] map;
    static boolean[][] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        L = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken());

        map = new int[n][n];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int result = 0;

        // 계란 이동이 더 이상 필요 없을 때까지 반복
        boolean flag = true;
        while (flag) {
            flag = false;

            visited = new boolean[n][n]; // 방문 배열 초기화

            // 모든 계란틀 검사
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // 아직 방문하지 않았고, 이동할 수 있는 경우
                    if (!visited[i][j] && checkEggMold(i, j)) {
                        flag = true;
                    }
                }
            }

            // 계란 이동이 일어난 경우 횟수 증가
            if (flag) {
                result++;
            }
        }

        System.out.println(result);
    }

    // 계란을 이동시킬 계란틀 찾기
    private static boolean checkEggMold(int x, int y) {
        Queue<int[]> q = new LinkedList<>();
        ArrayList<int[]> mold = new ArrayList<>(); // 합쳐질 계란틀 저장

        q.add(new int[] {x, y});
        visited[x][y] = true;

        mold.add(new int[] {x, y});

        int total = map[x][y]; // 합쳐질 계란의 총 합

        while (!q.isEmpty()) {
            int[] cur = q.poll();

            for (int i = 0; i < 4; i++) {
                int nx = cur[0] + dx[i];
                int ny = cur[1] + dy[i];

                // 범위 벗어나거나 이미 방문한 경우
                if (!checkRange(nx, ny) || visited[nx][ny]) {
                    continue;
                }

                int diff = Math.abs(map[cur[0]][cur[1]] - map[nx][ny]); // 인접한 계란틀과의 차이를 구함

                // 계란 양의 차이가 범위 안에 있을 경우
                if (diff >= L && diff <= R) {
                    q.add(new int[] {nx, ny});
                    visited[nx][ny] = true;

                    mold.add(new int[] {nx, ny});
                    total += map[nx][ny];
                }
            }
        }

        // 이동이 일어나지 않는 경우 false 반환
        if (mold.size() == 1) {
            return false;
        }

        moveEgg(mold, total);
        return true;
    }

    // 계란 이동시키기
    private static void moveEgg(ArrayList<int[]> mold, int total) {
        int move = total / mold.size(); // (합쳐진 계란의 총 합)/(합쳐진 계란틀의 총 개수

        for (int[] point : mold) {
            map[point[0]][point[1]] = move; // 합쳐진 계란틀에 계산된 계란의 양을 넣어줌
        }
    }

    // 범위 확인
    private static boolean checkRange(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < n;
    }
}