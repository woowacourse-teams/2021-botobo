import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import DeleteIcon from '../assets/cross-mark.svg';
import { useSnackbar } from '../hooks';
import { Flex } from '../styles';
import { TagResponse } from '../types';

interface Props {
  hashtags: TagResponse[];
  setHashtags: React.Dispatch<React.SetStateAction<TagResponse[]>>;
}

const MAX_HASHTAG_COUNT = 5;
const MAX_HASHTAG_LENGTH = 20;

const Hashtag = ({ hashtags, setHashtags }: Props) => {
  const [value, setValue] = useState('');
  const showSnackbar = useSnackbar();

  const submitHashtag: React.KeyboardEventHandler<HTMLInputElement> = (
    event
  ) => {
    if (event.key !== 'Enter' || value === '') return;

    event.preventDefault();

    if (value.length > MAX_HASHTAG_LENGTH) {
      showSnackbar({ message: '해시태그는 20자 이하로 입력해주세요.' });

      return;
    }

    setValue('');

    if (hashtags.map(({ name }) => name).includes(value)) return;

    setHashtags((prevState) => [...prevState, { name: value, id: 0 }]);
  };

  return (
    <Container>
      {hashtags.map(({ name }) => (
        <Tag key={name}>
          <Hash>#</Hash>
          {name}
          <DeleteButton
            onClick={() =>
              setHashtags((prevState) =>
                prevState.filter((tag) => tag.name !== name)
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
