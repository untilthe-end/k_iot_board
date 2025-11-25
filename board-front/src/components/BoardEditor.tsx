import { boardApi } from '@/apis/board/board.api';
import type { BoardListResponse } from '@/types/board/board.dto';
import { downloadFile } from '@/utils/download';
import styled from '@emotion/styled';
import React, { useEffect, useRef, useState } from 'react'

const Wrap = styled.div`
  border: 1px solid black;
  padding: 12px;
  border-radius: 8px;
  max-width: 800px;
`;

const Row = styled.div`
  margin-bottom: 12px;
`;

const Btn = styled.button`
  padding: 6px 12px;
  border-radius: 8px;
  margin-right: 8px;
`;

type Mode = "create" | "edit";

type BoardEditorProps = {
  mode?: Mode;
  boardId?: number; // edit 모드 일 때 필요
  onSaved?: (boardId?: number) => void;
}

//! 게시글 생성 & 게시글 수정
function BoardEditor({ mode = "create", boardId, onSaved } : BoardEditorProps) {
  // 기존 파일 목록 (편집 모드에서 불러와 체크박스로 유지/삭제)
  const [existingFiles, setExistingFiles] = useState<BoardListResponse>([]);
  const [keepIds, setKeepIds] = useState<number[]>([]);
  const [newFiles, setNewFiles] = useState<File[]>([]);

  const fileInputRef = useRef<HTMLInputElement | null>(null);

  useEffect(() => {
    if (mode === 'edit' && boardId) {
      fetchExistingFiles();
    }
  }, [mode, boardId]);

  const fetchExistingFiles = async () => {
    try {
      if (boardId) {
        const data = await boardApi.getFilesByBoard(boardId);
        setExistingFiles(data ?? []);
        setKeepIds(data.map(file => file.fileId));
      }
    } catch (e) { 
      console.error(e);
    }
  }

  // 파일 입력 변경 (다중)
  const onFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const list = e.target.files ? Array.from(e.target.files) : [];
    setNewFiles(prev => [...prev, ...list]);
    if (fileInputRef.current) fileInputRef.current.value = '';
  }

  const toggleKeep = (fileId: number) => {
    setKeepIds(prev => (
      // 기존 목록에 체크박스 클릭한 요소가 있을 때
      prev.includes(fileId) 
        // 기존 목록에서 삭제
        ? prev.filter(id => id !== fileId) 
        // 기존 목록에 추가
        : [...prev, fileId]));
  }

  const removeNewFile = (index: number) => {
    setNewFiles(prev => prev.filter((_, i) => i !== index));
  }

  const createBoard = async () => {
    const fd = new FormData();

    newFiles.forEach(file => {
      fd.append("files", file);
    });

    try {
      console.log(boardId);
      if (boardId) {
        boardApi.uploadBoardFiles(boardId, fd);
        alert('게시글 생성 완료');
        onSaved?.();
      }
    } catch (e: any) {
      console.error(e);
      alert(e?.response?.data?.message ?? "생성 실패");
    }
  }

  const updateFiles = async () => {

  }

  return (
    <Wrap>
      <Row>
        <h4>게시글 제목</h4>
        <input type="text" />
      </Row>
      <Row>
        <h4>게시글 내용</h4>
        <textarea name="" id=""></textarea>
      </Row>
      <Row>
        <h4>게시글 첨부파일</h4>
        <div>
          <div style={{ marginBottom: 6 }}>기존 첨부파일 (체크 해제 = 삭제)</div>
          {existingFiles.length === 0 && <div>없음</div>}
          {existingFiles.map(file => (
            <label key={file.fileId} style={{
              display: 'flex',
              alignItems: 'center',
              gap: 8,
              marginBottom: 6
            }}>
              <input 
                type="checkbox" 
                checked={keepIds.includes(file.fileId)} 
                onChange={() => toggleKeep(file.fileId)}
              />
              <span>{file.originalName}</span>
              <button 
                type='button'
                onClick={() => downloadFile(file.fileId, file.originalName)}
                style={{ marginLeft: 8 }}
              >다운로드</button>
            </label>
          ))}
        </div>
      </Row>

      <Row>
        <div style={{ marginBottom: 6 }}>새로 추가할 파일</div>
        <input 
          type="file" 
          ref={fileInputRef} 
          multiple 
          onChange={onFileChange}
        />
        <div style={{ marginTop: 8 }}>
          {newFiles.map((file, index) => (
            <div key={index} style={{
              display: 'flex',
              alignItems: 'center',
              gap: 8,
              marginBottom: 6
            }}>
              <span>{file.name}</span>
              <button onClick={() => removeNewFile(index)}>제거</button>
            </div>
          ))}
        </div>
      </Row>
      <Row>
        {mode === "create" ? (
          <div>
            <Btn onClick={createBoard}>게시글 생성</Btn>
          </div>
        ) : (
          <div>
            <Btn onClick={updateFiles}>첨부파일 수정 저장</Btn>
          </div>
        )}
      </Row>
    </Wrap>
  )
}

export default BoardEditor