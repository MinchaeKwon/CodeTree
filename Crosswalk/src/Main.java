/**
 * 보도블럭
 * 삼성 SW 역량테스트 2017 하반기 오전 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/crosswalk/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 3.
 * 
 * 백준 14890 경사로
 * https://www.acmicpc.net/problem/14890
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static int n, L;
    static int[][] map;

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        L = Integer.parseInt(st.nextToken());

        map = new int[n][n];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int result = 0;

        for (int i = 0; i < n; i++) {
            // 행 확인
            if (checkBlock(i, 0, 0)) {
                result++;
            }

            // 열 확인
            if (checkBlock(0, i, 1)) {
                result++;
            }
        }

        System.out.println(result);
    }

    // 행 또는 열의 위치, 행인지 열인지 구분하는 변수(0이면 행, 1이면 열)
    private static boolean checkBlock(int x, int y, int type) {
        int[] height = new int[n]; // 행 또는 열의 높이를 저장

        for (int i = 0; i < n; i++) {
            height[i] = type == 0 ? map[x][y + i] : map[x + i][y]; // 행일 경우 y축만 증가, 열일 경우 x축만 증가
        }

        boolean[] visited = new boolean[n]; // 행 또는 열의 칸에 경사로가 놓였는지 확인

        for (int i = 0; i < n - 1; i++) {
            int diff = height[i] - height[i + 1]; // 다음 칸과의 차이를 구함

            // 높이 차가 1을 초과하는 경욱 경사로를 놓을 수 없기 때문에 false 반환
            if (Math.abs(diff) > 1) {
                return false;
            }

            if (diff == 1) { // 내려가는 경우
                // 현재 칸보다 (i + 1)칸의 숫자가 하나 작기 때문에 (i + 1)칸부터 L개를 탐색하면서 (i + 1)칸과 숫자가 같은지, 경사로가 이미 있는지 확인해야함
                for (int j = i + 1; j <= i + L; j++) {
                    // 범위 벗어나거나, 이미 경사로 있거나, 높이가 다를 경우 (L개의 경사로를 놓을 수 없음)
                    if (j >= n ||  visited[j] || height[i + 1] != height[j]) {
                        return false;
                    }

                    visited[j] = true;
                }
            } else if (diff == -1) {// 올라가는 경우
                // (i + 1)칸에 있는 숫자가 현재 칸보다 숫자가 크기 때문에 현재 칸을 포함해서 이전 L칸에 있는 숫자가 현재 칸과 같은지, 경사로가 이미 있는지 확인해야함
                for (int j = i; j > i - L; j--) {
                    // 범위 벗어나거나, 이미 경사로 있거나, 높이가 다를 경우 (L개의 경사로를 놓을 수 없음)
                    if (j < 0 ||  visited[j] || height[i] != height[j]) {
                        return false;
                    }

                    visited[j] = true;
                }
            }
        }

        return true;
    }
}