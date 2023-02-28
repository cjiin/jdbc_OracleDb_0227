package com.kh.jdbc.dao;
import com.kh.jdbc.util.Common;
import com.kh.jdbc.vo.EmpVO;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmpDAO {
    Connection conn = null; // 자바와 오라클에 대한 연결 설정
    Statement stmt = null; // SQL문을 수행하기 위한 객체
    PreparedStatement pStmt = null;
    ResultSet rs = null; // statement 동작에 대한 결과로 전달되는 DB의 내용

    Scanner sc = new Scanner(System.in);

    public List<EmpVO> empSelect() {
        List<EmpVO> list = new ArrayList<>(); // 반환할 리스트를 위해 리스트 객체 생성
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM EMP";
            rs = stmt.executeQuery(sql); // select 문과 같이 여러개의 레코드(행)로 결과가 반환 될 때 사용

            while (rs.next()) { //읽을 행이 있으면 참
                int no = rs.getInt("EMPNO");
                String name = rs.getString("ENAME");
                String job = rs.getString("JOB");
                int mgr = rs.getInt("MGR");
                Date date = rs.getDate("HIREDATE");
                BigDecimal sal = rs.getBigDecimal("SAL");
                BigDecimal comm = rs.getBigDecimal("COMM");
                int deptNo = rs.getInt("DEPTNO");
                EmpVO vo = new EmpVO(no, name, job, mgr, date, sal, comm, deptNo); // 하나의 행(레코드)에 대한 정보 저장을 위한 객체 생성
                list.add(vo);
            }
            Common.close(rs); // 연결과 역순으로 해제
            Common.close(stmt);
            Common.close(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public void empSelectPrint(List<EmpVO> list) {
        for (EmpVO e : list) {
            System.out.println("사원 번호 : " + e.getNo());
            System.out.println("이름 : " + e.getName());
            System.out.println("직책 : " + e.getJob());
            System.out.println("매니저 : " + e.getMgr());
            System.out.println("입사일 : " + e.getDate());
            System.out.println("급여 : " + e.getSal());
            System.out.println("성과급 : " + e.getComm());
            System.out.println("부서번호 : " + e.getDeptNo());
            System.out.println("=========================");
        }
    }

    public void empInsert() {
        System.out.println("사원 정보를 입력하세요.");
        System.out.print("사원번호(4자리) : ");
        int no = sc.nextInt();
        System.out.print("이름 : ");
        String name = sc.next();
        System.out.print("직책 : ");
        String job = sc.next();
        System.out.print("매니저(4자리) : ");
        int mgr = sc.nextInt();
//        System.out.print("입사일 : ");
//        String date = sc.next();
        System.out.print("급여 : ");
        BigDecimal sal = sc.nextBigDecimal();
        System.out.print("성과급 : ");
        BigDecimal comm = sc.nextBigDecimal();
        System.out.print("부서번호 : ");
        int dept = sc.nextInt();

//        String sql = "INSERT INTO EMP(EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO) VALUES("
//                + no + ", " + "'" + name + "'" + ", " + "'" + job + "'" + ", " +  mgr + ", " + "'" + date + "'" + ", "
//                + sal + ", " + comm + ", " + dept + ")";
        String sql = "INSERT INTO EMP(EMPNO,ENAME,JOB,MGR,HIREDATE, SAL, COMM, DEPTNO) VALUES (?,?,?,?,SYSDATE,?,?,?)";

        try {
            conn = Common.getConnection();
            // stmt = conn.createStatement();
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, no);
            pStmt.setString(2, name);
            pStmt.setString(3, job);
            pStmt.setInt(4, mgr);
            // pStmt.setString(5,date);
            pStmt.setBigDecimal(5, sal);
            pStmt.setBigDecimal(6, comm);
            pStmt.setInt(7, dept);
            // int ret = stmt.executeUpdate(sql); // 반환 값 정수타입. 영향받는 행이 1개면 1이 넘어오도록
            int ret = pStmt.executeUpdate();
            System.out.println("Return : " + ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pStmt);
        Common.close(conn);
    }

    public void empUpdate() {
        System.out.print("변경할 사원의 이름을 입력하세요 : ");
        String name = sc.next();
        System.out.print("직책 : ");
        String job = sc.next();
        System.out.print("급여 : ");
        BigDecimal sal = sc.nextBigDecimal();
        System.out.print("성과급 : ");
        BigDecimal comm = sc.nextBigDecimal();

        String sql = "UPDATE EMP SET JOB = ?, SAL = ?, COMM = ? WHERE ENAME = ?";

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, job);
            pStmt.setBigDecimal(2, sal);
            pStmt.setBigDecimal(3, comm);
            pStmt.setString(4, name); // 물음표 순서대로 넣기
            pStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pStmt);
        Common.close(conn);
    }

    public void empDelete() {
        System.out.println("삭제할 이름을 입력 하세요 : ");
        String name = sc.next();
        String sql = "DELETE FROM EMP WHERE ENAME = ?";

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, name);
            pStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pStmt);
        Common.close(conn);
    }
}
