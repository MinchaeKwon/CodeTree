/**
 * 마법의 숲 탐색
 * https://www.codetree.ai/training-field/frequent-problems/problems/magical-forest-exploration/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2024. 5. 18.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	static class Pair {
		int x;
		int y;
		
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	// 북동남서
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
 	
	static int R, C, K;
	
	static int[][] map; // 골렘 번호 저장
	static boolean[][] exit; // 골렘 출구인지 저장
	
	static int answer;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        
        map = new int[R + 3][C];
        exit = new boolean[R + 3][C];
        
        for (int i = 1; i <= K; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int y = Integer.parseInt(st.nextToken()) - 1; // 골렘이 출발하는 열
        	int d = Integer.parseInt(st.nextToken()); // 출구 방향
        	
        	down(0, y, d, i);
        }

        System.out.println(answer);
	}
	
	private static void down(int x, int y, int d, int id) {
		if (check(x + 1, y)) {
			// 1. 남쪽으로 이동
			down(x + 1, y, d, id);
		} else if (check(x + 1, y - 1)) {
			// 2. 남쪽으로 이동하지 못하면 서쪽으로 회전 후 남쪽으로 이동
			down(x + 1, y - 1, (d + 3) % 4, id);
		} else if (check(x + 1, y + 1)) {
			// 3. 2번으로도 이동하지 못하면 동쪽으로 회전 후 남쪽으로 이동
			down(x + 1, y + 1, (d + 1) % 4, id);
		} else {
			// 가장 밑으로 내려갈 수 있을 때까지 진행
			
			// 위아래 칸이 숲의 범위를 벗어나는지 확인
			if (!isForest(x - 1, y) || !isForest(x + 1, y)) {
				// 골렘의 숲의 범위를 벗어나는 경우 모든 골렘 숲에서 없앰
				reset();
			} else {
				// 골렘 정착
				map[x][y] = id;
				
				for (int i = 0; i < 4; i++) {
					map[x + dx[i]][y + dy[i]] = id;
				}
				
				exit[x + dx[d]][y + dy[d]] = true;
				
				// 맵의 크기를 +3 해줬기 때문에 -3해줌, 0부터 시작했기 때문에 +1 해줌
				answer += bfs(x, y) - 3 + 1;
			}
		}
	}
	
	// 골렘이 위에서 아래로 이동할 수 있는지 확인
	// 현재 위치와 이동 전 위치 확인 - 중심 (x, y) (x - 1, y) 확인
	// down 함수에서 이동할 위치를 확인하기 때문에 -1한 위치도 확인하는 것
	private static boolean check(int x, int y) {
		// 어차피 남쪽으로 이동하기 때문에 좌, 우, 아래만 확인하면 됨
		return isRange(x, y) 
				&& map[x - 1][y - 1] == 0
				&& map[x - 1][y] == 0
				&& map[x - 1][y + 1] == 0
				&& map[x][y - 1] == 0
				&& map[x][y] == 0
				&& map[x][y + 1] == 0
				&& map[x + 1][y] == 0;
	}
	
	// 맵의 범위를 벗어나는지 확인
	private static boolean isRange(int x, int y) {
		return x - 1 >= 0 && x + 1 < R + 3 && y - 1 >= 0 && y + 1 < C;
	}
	
	// 숲의 범위를 벗어나는지 확인
	private static boolean isForest(int x, int y) {
		return x >= 3 && x < R + 3 && y >= 0 && y < C;
	}
	
	// 정령이 내려갈 수 있는 최하단 행의 위치 반환 - 정령의 위치에서 시작
	private static int bfs(int x, int y) {
		Queue<Pair> q = new LinkedList<>();
		boolean[][] visited = new boolean[R + 3][C];
		
		q.add(new Pair(x, y));
		visited[x][y] = true;
		
		int result = x; // 정령이 내려갈 수 있는 최대 행의 위치
		
		while (!q.isEmpty()) {
			Pair cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				// 숲의 범위를 벗어나거나 이미 방문한 경우 다음으로 넘어감
				if (!isForest(nx, ny) || visited[nx][ny]) {
					continue;
				}
				
				// 현재 골렘 안에서 움직일 수 있는 경우 || 다음 칸이 다른 골렘이고 현재 칸이 골렘의 출구라서 다른 골렘으로 옮겨갈 수 있는 경우
				if ((map[nx][ny] == map[cur.x][cur.y]) || (map[nx][ny] != 0 && exit[cur.x][cur.y])) {
					q.add(new Pair(nx, ny));
					visited[nx][ny] = true;
					
					result = Math.max(result, nx);
				}
			}
		}
		
		return result;
	}
	
	// 숲에 위치한 모든 골렘이 숲을 빠져나감
	private static void reset() {
		for (int i = 0; i < R + 3; i++) {
			Arrays.fill(map[i], 0);
			Arrays.fill(exit[i], false);
		}
	}

}
