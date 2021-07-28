import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import DeleteIcon from '../assets/cross-mark.svg';
import { Flex } from '../styles';

interface Props {
  hashtags: string[];
  setHashtags: React.Dispatch<React.SetStateAction<string[]>>;
}

const MAX_HASHTAG_COUNT = 3;

const Hashtag = ({ hashtags, setHashtags }: Props) => {
  const [value, setValue] = useState('');

  const submitHashtag: React.KeyboardEventHandler<HTMLInputElement> = (
    event
  ) => {
    if (event.key !== 'Enter') return;

    event.preventDefault();
    setValue('');

    if (hashtags.includes(value)) return;

    setHashtags((prevState) => [...prevState, value]);
  };

  return (
    <Container>
      {hashtags.map((tag) => (
        <Tag key={tag}>
          <Hash>#</Hash>
          {tag}
          <DeleteButton
            onClick={() =>
              setHashtags((prevState) =>
                prevState.filter((state) => state !== tag)
              )
            }
          >
            <DeleteIcon width="0.5rem" height="0.5rem" />
          </DeleteButton>
        </Tag>
      ))}

      {hashtags.length < MAX_HASHTAG_COUNT && (
        <InputWrapper>
          <span>#</span>
          <Input
            value={value}
            onChange={({ target }) => setValue(target.value)}
            onKeyPress={submitHashtag}
            name="hashtag"
            placeholder="태그입력"
          />
        </InputWrapper>
      )}
    </Container>
  );
};

const Container = styled.div`
  margin-top: 2rem;
  margin-left: 0.5rem;
`;

const Hash = styled.span`
  margin-right: 0.1rem;
`;

const Tag = styled.div`
  line-height: 1.5;
  word-break: break-all;

  margin-bottom: 0.3rem;
`;

const InputWrapper = styled.div`
  ${Flex({ items: 'center' })};
  margin-top: 0.5rem;

  ${({ theme }) => css`
    & > span {
      margin-right: 0.1rem;
      color: ${theme.color.gray_6};
    }
  `}
`;

const DeleteButton = styled.button`
  margin-left: 0.3rem;
`;

const Input = styled.input`
  width: 100%;
  outline: none;
  border: none;
  background-color: transparent;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.default};
  `}
`;

export default Hashtag;
