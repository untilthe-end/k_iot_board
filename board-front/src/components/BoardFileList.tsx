import { boardApi } from '@/apis/board/board.api';
import type { BoardFileListDto, BoardListResponse } from '@/types/board/board.dto';
import { downloadFile } from '@/utils/download';
import styled from '@emotion/styled';
import React, { useEffect, useState } from 'react'

interface BoardFileListProps {
  boardId: number;
  onChange?: () => void; 
}

const Wrap = styled.div`
  border: 1px solid black;
  padding: 12px;
  border-radius: 8px;
`;

const Row = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6px 0;
  border-bottom: 1px dashed #eee;
`;

const Name = styled.div`
  flex: 1;
  margin-right: 12px;
`;

const SmallBtn = styled.button`
  padding: 6px 8px;
  border-radius: 6px;
  margin-left: 6px;
`;

//! 게시글 파일 목록 (조회 / 다운로드 / 삭제)
function BoardFileList({ boardId, onChange }: BoardFileListProps) {
  //^ === HOOKS ===
  const [files, setFiles] = useState<BoardListResponse>([]);
  console.log(files);
  const [loading, setLoading] = useState<boolean>(false);

  const fetchFiles = async () => {
    setLoading(true);
    try {
      const data = await boardApi.getFilesByBoard(boardId);
      setFiles(data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // 컴포넌트 마운팅 + boardId(게시글 변경 시)
    // : 게시글 파일 목록 다시 fetch(가져오기)
    fetchFiles();
  }, [boardId]);

  //^ === EVENT HANDLER ===
  const onDownload = async (file: BoardFileListDto) => {
    await downloadFile(file.fileId, file.originalName);
  }

  const onDelete = async (fileId: number) => {
    if (!confirm("파일을 삭제하시겠습니까?")) return;

    try {
      await boardApi.DELETE_BOARD_FILE(fileId);
      setFiles(prev => prev.filter(p => p.fileId !== fileId));
      onChange?.();
    } catch (e: any) {
      console.error(e);
      alert(e?.response?.data?.message ?? "삭제 실패");
    }
  }

  return (
    <Wrap>
      <h4>첨부파일</h4>
      {loading ? <div>로딩중입니다.</div> : (
        <>
          {files.length === 0 && <div>첨부된 파일이 없습니다.</div>}
          {files.map(file => (
            <Row key={file.fileId}>
              <Name title={file.originalName}>{file.originalName}</Name>
              <img src={file.downloadUrl} alt="" />
              <div>
                <SmallBtn onClick={() => onDownload(file)}>다운로드</SmallBtn>
                <SmallBtn onClick={() => onDelete(file.fileId)}>삭제</SmallBtn>
              </div>
            </Row>
          ))}
        </>
      )}
    </Wrap>
  )
}

export default BoardFileList