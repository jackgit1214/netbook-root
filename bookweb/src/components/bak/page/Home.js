import Typography from '@material-ui/core/Typography/Typography';
import React from 'react';

function customHomer() {
    return (
        <div>
            <Typography component="h2" variant="h6" color="primary" gutterBottom>
                恭喜您打开首页面了，这说明路由配置成功了。
            </Typography>
        </div>
    )

}

export default customHomer;
