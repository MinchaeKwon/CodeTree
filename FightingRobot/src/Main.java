/**
 * 전투 로봇
 * 삼성 SW 역량테스트 2018 하반기 오후 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/fighting-robot/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 10.
 * 
 * 백준 16236 아기 상어
 * https://www.acmicpc.net/problem/16236
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {

    static class Robot implements Comparable<Robot> {
        int x;
        int y;
        int d; // 이동 거리

        public Robot(int x, int y, int d) {
            this.x = x;
            this.y = y;
            this.d = d;
        }

        @Override
        public int compareTo(Robot o) {
            if (this.d == o.d) {
                if (this.x == o.x) {
                    return this.y - o.y; // 행이 같으면 열을 기준으로 오름차순 정렬 (열 크기가 작은 것)
                }

                return this.x - o.x; // 거리가 같으면 행을 기준으로 오름차순 정렬 (행 크기가 작은 것)
            }

            return this.d - o.d; // 거리를 기준으로 오름차순 정렬
        }
    }

    // 상하좌우
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};

    static int n;
    static int[][] map;

    static int sx, sy; // 전투 로봇 초기 위치

    static int level = 2; // 초기 전투 로봇 레벨은 2
    static int moster = 0; // 없앤 몬스터 수
    static int time = 0; // 몬스터를 처리하는데 걸린 시간

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        n = Integer.parseInt(br.readLine());

        map = new int[n][n];

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());

                if (map[i][j] == 9) {
                    sx = i;
                    sy = j;

                    map[i][j] = 0; // 전투 로봇이 있던 자리는 0으로 만듦 (지나갈 수 있는 곳이기 때문)
                }
            }
        }
        
        // 더이상 공격할 수 없을 때까지 반복
        boolean attack = true;

        while (attack) {
            attack = moveRobot(sx, sy);

            if (!attack) {
                break;
            }

            // 전투 로봇의 레벨만큼 몬스터를 없앤 경우 레벨 증가, 몬스터 수 초기화
            if (level == moster) {
                level++;
                moster = 0;
            }
        }

        System.out.println(time);
    }

    // 전투 로봇 이동시키기
    private static boolean moveRobot(int x, int y) {
        PriorityQueue<Robot> q = new PriorityQueue<>(); // 우선순위 큐 사용해서 최소 거리의 몬스터 공격
        boolean[][] visited = new boolean[n][n];

        q.add(new Robot(x, y, 0));
        visited[x][y] = true;

        while (!q.isEmpty()) {
            Robot cur = q.poll();

            // 몬스터가 있고, 공격할 수 있는 경우(몬스터의 크기가 전투 로봇보다 작아야함)
            if (map[cur.x][cur.y] != 0 && map[cur.x][cur.y] < level) {
                map[cur.x][cur.y] = 0; // 몬스터를 없앴기 때문에 0을 넣어줌
                moster++; // 몬스터 수 증가
                time += cur.d; // 처리 시간 증가

                // 전투 로봇 위치 갱신
                sx = cur.x;
                sy = cur.y;

                return true;
            }

            // 상하좌우 4방향 확인
            for (int i = 0; i < 4; i++) {
                int nx = cur.x + dx[i];
                int ny = cur.y + dy[i];

                // 범위를 벗어나지 않고, 아직 방문하지 않았으며 전투 로봇이 지나칠 수 있는 칸일 경우 (몬스터의 크기가 전투 로봇의 레벨과 같거나 작아야함)
                if (checkRange(nx, ny) && !visited[nx][ny] && map[nx][ny] <= level) {
                    q.add(new Robot(nx, ny, cur.d + 1));
                    visited[nx][ny] = true;
                }
            }
        }

        return false; // 처리한 몬스터가 없을 경우 false 반환
    }

    // 범위 확인
    private static boolean checkRange(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < n;
    }
}