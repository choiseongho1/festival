import React, { useState, useEffect } from 'react';
import {
  Box,
  Button,
  TextField,
  Typography,
  Container,
  Paper,
  Snackbar,
  Alert
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { groupApi } from '../../api/groupApi';
import { userApi } from '../../api/userApi'; // 사용자 정보 조회를 위한 API

const GroupCreate = ({ festivalId }) => {
  const navigate = useNavigate();
  
  const [groupData, setGroupData] = useState({
    groupName: '',
    leaderId: '',
    festivalId: festivalId || '',
    maxMembers: '',
  });
  
  const [notification, setNotification] = useState({
    open: false,
    message: '',
    severity: 'success'
  });

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const userInfo = await userApi.getCurrentUser();
        setGroupData(prev => ({
          ...prev,
          leaderId: userInfo.id
        }));
      } catch (error) {
        console.error('사용자 정보 조회 실패:', error);
        showNotification('사용자 정보를 가져오는데 실패했습니다.', 'error');
      }
    };

    fetchUserInfo();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setGroupData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const showNotification = (message, severity = 'success') => {
    setNotification({
      open: true,
      message,
      severity
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const response = await groupApi.createGroup({
        ...groupData,
        maxMembers: Number(groupData.maxMembers)
      });

      showNotification('그룹이 성공적으로 생성되었습니다!');

      // 생성된 그룹의 상세 페이지로 이동
      setTimeout(() => {
        navigate(`/group/${response.id}`);
      }, 2000);

    } catch (error) {
      let errorMessage = '그룹 생성에 실패했습니다.';
      
      if (error.response?.data?.message?.includes('휴대폰 인증')) {
        errorMessage = '그룹 생성을 위해서는 휴대폰 인증이 필요합니다.';
      }

      showNotification(errorMessage, 'error');
      console.error('그룹 생성 오류:', error);
    }
  };

  const handleCloseNotification = () => {
    setNotification(prev => ({
      ...prev,
      open: false
    }));
  };

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
        <Typography variant="h5" component="h2" gutterBottom>
          새로운 축제 그룹 만들기
        </Typography>
        
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
          <TextField
            fullWidth
            required
            label="그룹 이름"
            name="groupName"
            value={groupData.groupName}
            onChange={handleChange}
            margin="normal"
            helperText="그룹을 대표하는 이름을 입력해주세요"
          />
          
          <TextField
            fullWidth
            required
            label="최대 인원"
            name="maxMembers"
            type="number"
            value={groupData.maxMembers}
            onChange={handleChange}
            margin="normal"
            inputProps={{ min: 2, max: 10 }}
            helperText="2명에서 10명까지 설정 가능합니다"
          />
          
          {!festivalId && (
            <TextField
              fullWidth
              required
              label="축제 ID"
              name="festivalId"
              value={groupData.festivalId}
              onChange={handleChange}
              margin="normal"
              helperText="참여하고자 하는 축제의 ID를 입력해주세요"
            />
          )}
          
          <Button
            type="submit"
            variant="contained"
            color="primary"
            fullWidth
            sx={{ mt: 3 }}
          >
            그룹 만들기
          </Button>
        </Box>
      </Paper>

      <Snackbar
        open={notification.open}
        autoHideDuration={6000}
        onClose={handleCloseNotification}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
      >
        <Alert
          onClose={handleCloseNotification}
          severity={notification.severity}
          sx={{ width: '100%' }}
        >
          {notification.message}
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default GroupCreate;