/**
 * 코드트리 빵
 * 삼성 SW 역량테스트 2022 하반기 오후 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/codetree-mon-bread/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 24.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

class Pair {
	int x;
	int y;
	
	public Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isSame(Pair p) {
		return x == p.x && y == p.y;
	}
}

public class Main {
	
	// 상좌우하 ↑, ←, →, ↓
	static int[] dx = {-1, 0, 0, 1};
	static int[] dy = {0, -1, 1, 0};
	
	static final Pair EMPTY = new Pair(-1, -1);
	
	static int n, m;
	
	static int[][] map; // 0 빈 공간, 1 베이스캠프, 2 지나갈 수 없는 곳
	static Pair[] cvs; // 편의점 위치 저장
	static Pair[] person; // 사람들의 위치 저장
	
	static boolean[][] visited;
	static int[][] step;
	
	static int time;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        map = new int[n][n];
        cvs = new Pair[m];
        person = new Pair[m];
        
        visited = new boolean[n][n];
        step = new int[n][n];
        
        for (int i = 0; i < n; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < n; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        for (int i = 0; i < m; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int x = Integer.parseInt(st.nextToken()) - 1;
        	int y = Integer.parseInt(st.nextToken()) - 1;
        	
        	cvs[i] = new Pair(x, y);
        	person[i] = EMPTY;
        }
        
        while (true) {
        	time++;
        	
        	move();
        	
        	if (isEnd()) {
        		break;
        	}
        }
        
        System.out.println(time);
        
	}
	
	private static void move() {
		// 편의점으로 이동시킴
		for (int i = 0; i < m; i++) {
			// 격자밖에 있거나 이미 편의점에 도착한 경우 다음으로 넘어감
			if (person[i] == EMPTY || person[i].isSame(cvs[i])) {
				continue;
			}
			
			// 본인이 가고 싶은 편의점으로 가는 최단거리 찾음
			bfs(cvs[i]);
			
			// 현재 위치에서 상좌우하 최단거리 값이 가장 작은 것을 고르면 해당 거리가 최단거리
			int minD = Integer.MAX_VALUE;
			int minX = 0;
			int minY = 0;
			
			for (int d = 0; d < 4; d++) {
				int nx = person[i].x + dx[d];
				int ny = person[i].y + dy[d];
				
				if (checkRange(nx, ny) && visited[nx][ny] && step[nx][ny] < minD) {
					minD = step[nx][ny];
					minX = nx;
					minY = ny;
				}
			}
			
			person[i] = new Pair(minX, minY);
		}
		
		// 편의점에 도착한 사람이 있으면 맵에 다른 사람은 지나갈 수 없다는 표시를 해줌 (사람들이 모두 이동한 뒤에 해주는 것)
		for (int i = 0; i < m; i++) {
			if (person[i].isSame(cvs[i])) {
				map[person[i].x][person[i].y] = 2;
			}
		}
		
		// 현재 시간이 m보다 작거나 같을 경우에만 베이스캠프로 감
		if (time > m) {
			return;
		}
		
		// t번 사람은 자신이 가고 싶은 편의점과 가장 가까이 있는 베이스 캠프에 들어가기 위해 편의점 위치부터 시작
		bfs(cvs[time - 1]); // 배열은 0부터 시작하기 때문에 -1해서 넣어줌
		
		int minD = Integer.MAX_VALUE;
		int minX = 0;
		int minY = 0;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				// 경로 상에 있고 베이스캠프이고 최단거리인 경우
				if (visited[i][j] && map[i][j] == 1 && step[i][j] < minD) {
					minD = step[i][j];
					minX = i;
					minY = j;
				}
			}
		}
		
		person[time - 1] = new Pair(minX, minY);
		map[minX][minY] = 2; // 특정 사람이 베이스캠프로 가면 다른 사람은 지나갈 수 없다는 표시를 해줌 (사람들이 모두 이동한 뒤에 해주는 것)
	}
	
	/*
	 * 원래는 현재 위치에서 편의점 위치까지의 최단거리를 구해줘야 함
	 * 다만 최단거리가 되기 위한 그 다음 위치를 구하기 위해서는 거꾸로 편의점 위치를 시작으로 현재 위치까지 오는 최단거리를 구해주는 것이 필요
	 * 따라서 편의점 위치를 시작으로 하는 BFS를 진행
	 * */
	
	// 특정 칸까지의 최단거리 찾음 (bfs 이용)
	private static void bfs(Pair start) {
		// 초기화
		for (int i = 0; i < n; i++) {
			Arrays.fill(visited[i], false);
			Arrays.fill(step[i], 0);
		}
		
		Queue<Pair> q = new LinkedList<>();
		
		q.add(start);
		visited[start.x][start.y] = true;
		
		while (!q.isEmpty()) {
			Pair cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				// 범위를 벗어나지 않고 방문하지 않았으며 이동가능한 곳일 경우
				if (checkRange(nx, ny) && !visited[nx][ny] && map[nx][ny] != 2) {
					q.add(new Pair(nx, ny));
					visited[nx][ny] = true;
					step[nx][ny] = step[cur.x][cur.y] + 1; // 다음 칸까지의 경로 길이기 때문에 +1 해줌
				}
			}
		}
	}
	
	// 모든 사람들이 편의점에 도착했는지 확인
	private static boolean isEnd() {
		for (int i = 0; i < m; i++) {
			if (!person[i].isSame(cvs[i])) {
				return false; // 한명이라도 도착하지 않은 경우 false 반환
			}
		}
		
		return true;
	}
	
	private static boolean checkRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
