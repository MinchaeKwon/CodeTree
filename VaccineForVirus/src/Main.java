/**
 * 바이러스 백신
 * 삼성 SW 역량테스트 2019 상반기 오후 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/vaccine-for-virus/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 8. 28.
 * 
 * 백준 17142 연구소 3
 * https://www.acmicpc.net/problem/17142
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static class Position {
        int x;
        int y;
        int time;

        public Position(int x, int y, int time) {
            this.x = x;
            this.y = y;
            this.time = time;
        }
    }

    // 상하좌우
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};

    static int N, M;
    static int[][] map; // 0 바이러스, 1 벽, 2 병원

    static int virusCnt = 0;
    static ArrayList<Position> hospitalList = new ArrayList<>();

    static int result = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new int[N][N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());

                if (map[i][j] == 0) {
                    virusCnt++;
                }else if (map[i][j] == 2) {
                    hospitalList.add(new Position(i, j, 0));
                }
            }
        }

        if (virusCnt == 0) {
            System.out.println(0); // 바이러스가 없는 경우에는 바로 0출력
        } else {
            pickHospital(0, 0);
            System.out.println(result == Integer.MAX_VALUE ? -1 : result); // 모든 바이러스를 없앨 수 없는 경우에는 -1 출력
        }
    }

    // M개의 병원 선택하기 (start부터 병원 리스트 크기만큼 for문 돌리면서 확인)
    private static void pickHospital(int start, int depth) {
        // M개 선택한 경우 바이러스 없앰
        if (depth == M) {
            removeVirus(virusCnt);
            return;
        }

        for (int i = start; i < hospitalList.size(); i++) {
            Position cur = hospitalList.get(i);

            map[cur.x][cur.y] = 3; // 병원이 선택된 경우는 3으로 표시
            pickHospital(i + 1, depth + 1);
            map[cur.x][cur.y] = 2; // 원복
        }
    }

    // 메모리 초과 발생하는 코드
    // private static void pickHospital(int depth) {
    //     // M개 선택한 경우 바이러스 없앰
    //     if (depth == M) {
    //         removeVirus(virusCnt);
    //         return;
    //     }

    //     for (int i = 0; i < N; i++) {
    //         for (int j = 0; j < N; j++) {
    //             if (map[i][j] == 2) {
    //                 map[i][j] = 3; // 병원이 선택된 경우는 3으로 표시
    //                 pickHospital(depth + 1);
    //                 map[i][j] = 2; // 원복
    //             }
    //         }
    //     }
    // }

    // 바이러스 없애기
    private static void removeVirus(int virus) {
        Queue<Position> q = new LinkedList<>();
        boolean[][] visited = new boolean[N][N];

        // 병원 위치 큐에 넣어줌
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (map[i][j] == 3) {
                    q.add(new Position(i, j, 0));
                    visited[i][j] = true;
                }
            }
        }

        // 선택된 병원 큐만큼 돌면서 바이러스 없앰
        while (!q.isEmpty()) {
            Position cur = q.poll();

            for (int i = 0; i < 4; i++) {
                int nx = cur.x + dx[i];
                int ny = cur.y + dy[i];
                
                // 범위 벗어나지 않고, 방문하지 않았으며 벽이 아닌 경우 (병원도 방문 가능함)
                if (checkRange(nx, ny) && !visited[nx][ny] && map[nx][ny] != 1) {
                    q.add(new Position(nx, ny, cur.time + 1));
                    visited[nx][ny] = true;

                    // 바이러스가 있는 곳이면 개수 감소
                    if (map[nx][ny] == 0) {
                        virus--;
                    }

                    // 모든 바이러스를 없앤 경우 최솟값 갱신
                    if (virus == 0) {
                        result = Math.min(result, cur.time + 1);
                        return;
                    }
                }
            }
        }
    }

    private static boolean checkRange(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < N;
    }
}